package com.sazonov.utility.manager;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.OutputType;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

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
        for (BufferedWriter writer : writers.values()) {
            close(writer);
        }
        writers.clear();
    }

    private boolean writeLine(OutputType type, String value) {
        BufferedWriter writer = writers.computeIfAbsent(type, this::openWriter);
        if (writer == null) {
            return false;
        }
        try {
            writer.write(value);
            writer.newLine();
            return true;
        } catch (IOException e) {
            failed.put(type, true);
            return false;
        }
    }

    private BufferedWriter openWriter(OutputType type) {
        if (Boolean.TRUE.equals(failed.get(type))) {
            return null;
        }
        Path outputPath = config.outputDirectory().resolve(config.prefix() + type.getFileName());
        try {
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
            failed.put(type, true);
            return null;
        }
    }

    private void close(BufferedWriter writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (IOException e) {
        }
    }
}
