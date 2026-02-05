package com.sazonov.utility.processor;

import com.sazonov.utility.manager.FileOutputManager;
import com.sazonov.utility.model.DataType;
import com.sazonov.utility.stats.StatsTracker;
import com.sazonov.utility.utils.LineClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class FileFilterProcessor {
    private final FileOutputManager outputManager;
    private final StatsTracker statsTracker;

    public void process(List<Path> inputFiles) {
        for (Path inputFile : inputFiles) {
            if (!Files.exists(inputFile)) {
                log.error("Input file not found: {}", inputFile);
                continue;
            }
            if (!Files.isReadable(inputFile)) {
                log.error("Input file is not readable: {}", inputFile);
                continue;
            }
            try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    handleLine(line);
                }
            } catch (IOException e) {
                log.error("Failed to read file {}: {}", inputFile, e.getMessage());
            }
        }
    }

    private void handleLine(String line) {
        DataType type = LineClassifier.classify(line);
        switch (type) {
            case INTEGER -> {
                log.debug("Detected INTEGER: {}", line);
                if (outputManager.writeInteger(line)) {
                    statsTracker.recordInteger(line.trim());
                }
            }
            case FLOAT -> {
                log.debug("Detected FLOAT: {}", line);
                if (outputManager.writeFloat(line)) {
                    statsTracker.recordFloat(line.trim());
                }
            }
            case STRING -> {
                log.debug("Detected STRING: {}", line);
                if (outputManager.writeString(line)) {
                    statsTracker.recordString(line);
                }
            }
            default -> log.debug("Unrecognized line type: {}", line);
        }
    }
}