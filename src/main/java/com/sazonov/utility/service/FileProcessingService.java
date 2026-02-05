package com.sazonov.utility.service;

import com.sazonov.utility.commandline.ArgumentParser;
import com.sazonov.utility.commandline.CliOptionsFactory;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.manager.FileOutputManager;
import com.sazonov.utility.processor.FileFilterProcessor;
import com.sazonov.utility.stats.StatsPresenter;
import com.sazonov.utility.stats.StatsTracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FileProcessingService {

    private final ArgumentParser argumentParser;

    public void process(String[] args) {
        log.info("Starting FileFilterUtility");

        Optional<Configuration> configOptional = argumentParser.parse(args);
        if (configOptional.isEmpty()) {
            log.error("Failed to parse CLI arguments.");
            argumentParser.printHelp();
            return;
        }

        Configuration config = configOptional.get();

        if (config.inputFiles() == null || config.inputFiles().isEmpty()) {
            log.error("No input files provided.");
            argumentParser.printHelp();
            return;
        }

        if (!ensureOutputDirectory(config.outputDirectory())) {
            log.error("Cannot continue: output directory is not available: {}", config.outputDirectory());
            return;
        }

        FileOutputManager outputManager = new FileOutputManager(config);
        StatsTracker statsTracker = new StatsTracker();
        FileFilterProcessor processor = new FileFilterProcessor(outputManager, statsTracker);
        StatsPresenter presenter = new StatsPresenter(config.statisticsMode());

        processFiles(config, processor, outputManager, statsTracker, presenter);
    }

    private boolean ensureOutputDirectory(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            log.debug("Output directory ready: {}", outputDirectory);
            return true;
        } catch (IOException e) {
            log.error("Failed to create output directory {}: {}", outputDirectory, e.getMessage());
            return false;
        }
    }

    private void processFiles(
            Configuration config,
            FileFilterProcessor processor,
            FileOutputManager outputManager,
            StatsTracker statsTracker,
            StatsPresenter presenter
    ) {
        log.info("Starting file processing...");

        List<Path> validFiles = config.inputFiles().stream()
                .filter(this::isValidInputFile)
                .toList();

        if (validFiles.isEmpty()) {
            log.error("Cannot continue: no readable input files were provided.");
            return;
        }

        try {
            processor.process(validFiles);
        } catch (Exception e) {
            // на всякий случай: чтобы программа не падала
            log.error("Unexpected error during processing: {}", e.getMessage(), e);
        } finally {
            try {
                outputManager.closeAll();
            } catch (Exception e) {
                log.error("Error while closing output files: {}", e.getMessage(), e);
            }
        }

        log.info("File processing completed.");
        presenter.print(statsTracker);
    }

    private boolean isValidInputFile(Path inputFile) {
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