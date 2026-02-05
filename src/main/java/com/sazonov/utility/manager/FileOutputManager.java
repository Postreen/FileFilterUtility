package com.sazonov.utility.manager;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.OutputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public final class FileOutputManager {
    private final Configuration config;
    private final Map<OutputType, BufferedWriter> writers = new EnumMap<>(OutputType.class);
    private final Map<OutputType, Boolean> failed = new EnumMap<>(OutputType.class);

    public boolean writeInteger(String value) {
        return writeLine(OutputType.INTEGER, value);
    }

    public boolean writeFloat(String value) {
        return writeLine(OutputType.FLOAT, value);
    }

    public boolean writeString(String value) {
        return writeLine(OutputType.STRING, value);
    }

    public void closeAll() {
        log.info("Closing all writers.");
        for (BufferedWriter writer : writers.values()) {
            close(writer);
        }
        writers.clear();
    }

    private boolean writeLine(OutputType type, String value) {
        BufferedWriter writer = writers.computeIfAbsent(type, this::openWriter);
        if (writer == null) {
            log.warn("Writer for {} is not available, skipping write.", type.getLabel());
            return false;
        }
        try {
            writer.write(value);
            writer.newLine();
            return true;
        } catch (IOException e) {
            log.error("Failed to write {} value: {}", type.getLabel(), e.getMessage());
            failed.put(type, true);
            return false;
        }
    }

    private BufferedWriter openWriter(OutputType type) {
        if (Boolean.TRUE.equals(failed.get(type))) {
            log.warn("Writer for {} has previously failed, skipping opening writer.", type.getLabel());
            return null;
        }
        Path outputPath = config.outputDirectory().resolve(config.prefix() + type.getFileName());
        try {
            log.debug("Opening writer for {} at path: {}", type.getLabel(), outputPath);
            if (config.append()) {
                return Files.newBufferedWriter(
                        outputPath,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );
            }
            return Files.newBufferedWriter(
                    outputPath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            log.error("Failed to open output file {}: {}", outputPath, e.getMessage());
            failed.put(type, true);
            return null;
        }
    }

    private void close(BufferedWriter writer) {
        if (writer == null) {
            log.debug("Closing writer.");
            return;
        }
        try {
            writer.close();
        } catch (IOException e) {
            log.error("Failed to close output file: {}", e.getMessage());
        }
    }
}