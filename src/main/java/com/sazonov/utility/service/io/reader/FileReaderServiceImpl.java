package com.sazonov.utility.service.io.reader;

import com.sazonov.utility.service.io.reader.handler.LineHandler;
import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.service.io.writer.OutputWriterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileReaderServiceImpl implements FileReaderService {
    private final LineHandler lineHandler;

    @Override
    public void readLines(List<Path> inputFiles,
                          OutputWriterService outputWriterService,
                          StatisticTracker statisticTracker
    ) {

        for (Path inputFile : inputFiles) {
            if (!isReadableFile(inputFile)) {
                continue;
            }
            try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lineHandler.handle(line);
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
