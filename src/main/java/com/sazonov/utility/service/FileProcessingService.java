package com.sazonov.utility.service;

import com.sazonov.utility.commandline.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.service.io.reader.FileReaderService;
import com.sazonov.utility.service.io.statistic.StatisticReportService;
import com.sazonov.utility.service.io.statistic.StatisticReportServiceImpl;
import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.service.io.writer.OutputWriterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingService {
    private final CliParser cliParser;
    private final Configuration configuration;
    private final FileReaderService fileReaderService;
    private final OutputWriterService outputWriterService;
    private final StatisticReportService statisticReportService;

    public void process(String[] args) {
        log.info("Start file processing.");

        try {
            parseCliArguments(args);
            List<Path> inputFiles = configuration.getInputFiles();

            if (processFiles(inputFiles)) {
                statisticReportService.print();
            }

            log.info("File processing completed.");
        } catch (Exception e) {
            log.error("File processing failed: {}", e.getMessage(), e);
        }
    }

    private void parseCliArguments(String[] args) {
        try {
            cliParser.parse(args);
        } catch (Exception e) {
            log.error("Failed to parse CLI arguments: {}", e.getMessage());
            throw new RuntimeException("Failed to parse CLI arguments.");
        }
    }

    private boolean processFiles(List<Path> inputFiles) {
        boolean success = true;

        try {
            fileReaderService.readLines(inputFiles);
        } catch (IOException e) {
            log.error("Error during file reading process: {}", e.getMessage());
            success = false;
        } catch (RuntimeException e) {
            log.error("All files failed to be processed: {}", e.getMessage());
            success = false;
        } catch (Exception e) {
            log.error("Unexpected error during file processing: {}", e.getMessage());
            success = false;
        } finally {
            closeOutputFiles();
        }

        return success;
    }

    private void closeOutputFiles() {
        try {
            outputWriterService.closeAll();
        } catch (Exception e) {
            log.error("Error while closing output files: {}", e.getMessage());
            throw new RuntimeException("Failed to close output files.");
        }
    }
}