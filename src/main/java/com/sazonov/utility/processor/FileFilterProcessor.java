package com.sazonov.utility.processor;

import com.sazonov.utility.manager.FileOutputManager;
import com.sazonov.utility.model.DataType;
import com.sazonov.utility.stats.StatsTracker;
import com.sazonov.utility.utils.LineClassifier;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public final class FileFilterProcessor {
    private final FileOutputManager outputManager;
    private final StatsTracker statsTracker;

    public void process(List<Path> inputFiles) {
        for (Path inputFile : inputFiles) {
            if (!Files.exists(inputFile)) {
                continue;
            }
            if (!Files.isReadable(inputFile)) {
                continue;
            }
            try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    handleLine(line);
                }
            } catch (IOException e) {
            }
        }
    }

    private void handleLine(String line) {
        DataType type = LineClassifier.classify(line);
        switch (type) {
            case INTEGER -> {
                if (outputManager.writeInteger(line)) {
                    statsTracker.recordInteger(line.trim());
                }
            }
            case FLOAT -> {
                if (outputManager.writeFloat(line)) {
                    statsTracker.recordFloat(line.trim());
                }
            }
            case STRING -> {
                if (outputManager.writeString(line)) {
                    statsTracker.recordString(line);
                }
            }
        }
    }
}