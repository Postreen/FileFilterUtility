package com.sazonov.utility.io.reader;

import com.sazonov.utility.io.FileReaderService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class DefaultFileReaderService implements FileReaderService {
    @Override
    public void readLines(List<Path> inputFiles, Consumer<String> lineConsumer) {
        for (Path inputFile : inputFiles) {
            if (!isReadableFile(inputFile)) {
                continue;
            }
            try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lineConsumer.accept(line);
                }
            } catch (IOException e) {
                log.error("Failed to read file {}: {}", inputFile, e.getMessage());
            }
        }
    }

    private boolean isReadableFile(Path inputFile) {
        if (inputFile == null) {
            log.error("Input file path is null.");
            return false;
        }
        if (!Files.exists(inputFile)) {
            log.error("Input file not found: {}", inputFile);
            return false;
        }
        if (!Files.isRegularFile(inputFile)) {
            log.error("Input path is not a file: {}", inputFile);
            return false;
        }
        if (!Files.isReadable(inputFile)) {
            log.error("Input file is not readable: {}", inputFile);
            return false;
        }
        return true;
    }
}
