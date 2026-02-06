package com.sazonov.utility.service;

import com.sazonov.utility.commandline.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.service.io.FileProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FileProcessingCoordinatorService {

    public void process(String[] args) {
        log.info("Starting FileFilterUtility");

        CliParser cliParser = new CliParser();

        Optional<Configuration> configOptional = cliParser.parse(args);
        if (configOptional.isEmpty()) {
            log.error("Failed to parse CLI arguments.");
            cliParser.printHelp();
            return;
        }

        Configuration config = configOptional.get();

        if (config.inputFiles() == null || config.inputFiles().isEmpty()) {
            log.error("No input files provided.");
            cliParser.printHelp();
            return;
        }

        if (!ensureOutputDirectory(config.outputDirectory())) {
            log.error("Cannot continue: output directory is not available: {}", config.outputDirectory());
            return;
        }

        processFiles(config);
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

    private void processFiles(Configuration config) {
        log.info("Starting file processing...");

        List<Path> validFiles = config.inputFiles().stream()
                .filter(this::isValidInputFile)
                .toList();

        if (validFiles.isEmpty()) {
            log.error("Cannot continue: no readable input files were provided.");
            return;
        }

        FileProcessorService fileProcessorService = new FileProcessorService();

        fileProcessorService.processFiles(validFiles, config);
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