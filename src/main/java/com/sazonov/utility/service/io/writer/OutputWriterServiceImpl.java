package com.sazonov.utility.service.io.writer;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.OutputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public final class OutputWriterServiceImpl implements OutputWriterService {
    private final Map<OutputType, BufferedWriter> writers = new EnumMap<>(OutputType.class);
    private final Configuration configuration;

    @Override
    public boolean writeInteger(String value) {
        return writeLine(OutputType.INTEGER, value);
    }

    @Override
    public boolean writeFloat(String value) {
        return writeLine(OutputType.FLOAT, value);
    }

    @Override
    public boolean writeString(String value) {
        return writeLine(OutputType.STRING, value);
    }

    @Override
    public void closeAll() {
        log.debug("Closing all writers.");
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
            log.error("Failed to write to output file: {}. Error: {}", buildOutputPath(type), e.getMessage());
            return false;
        }
    }

    private BufferedWriter openWriter(OutputType type) {
        Path outputPath = buildOutputPath(type);
        try {
            log.debug("Opening writer for {} at path: {}", type.getLabel(), outputPath);

            StandardOpenOption[] options = configuration.getAppend() ?
                    new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND} :
                    new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};

            if (configuration.getAppend() && Files.exists(outputPath) && !Files.isWritable(outputPath)) {
                log.error("Permission denied for appending to file: {}", outputPath);
                return null;
            }

            return Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8, options);
        } catch (IOException e) {
            log.error("Failed to open output file {}: {}", outputPath, e.getMessage());
            return null;
        }
    }

    private Path buildOutputPath(OutputType type) {
        return configuration.getOutputDirectory().resolve(configuration.getPrefix() + type.getFileName());
    }

    private void close(BufferedWriter writer) {
        if (writer == null) {
            log.debug("Writer is already closed.");
            return;
        }
        try {
            writer.close();
        } catch (IOException e) {
            log.error("Failed to close output file: {}", e.getMessage());
        }
    }
}