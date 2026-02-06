package com.sazonov.utility.service;

import com.sazonov.utility.commandline.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.service.io.FileProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingCoordinatorService {
    private final FileProcessorService fileProcessorService;
    private final CliParser cliParser;
    private final Configuration configuration;

    public void process(String[] args) {
        log.info("Starting FileFilterUtility");

        Optional<Configuration> configOptional = cliParser.parse(args);
        if (configOptional.isEmpty()) {
            log.error("Failed to parse CLI arguments.");
            cliParser.printHelp();
            return;
        }

        if (configuration.getInputFiles() == null || configuration.getInputFiles().isEmpty()) {
            log.error("No input files provided.");
            cliParser.printHelp();
            return;
        }

        if (!ensureOutputDirectory(configuration.getOutputDirectory())) {
            log.error("Cannot continue: output directory is not available: {}", configuration.getOutputDirectory());
            return;
        }

        processFiles();
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

    private void processFiles() {
        log.info("Starting file processing...");

        List<Path> validFiles = configuration.getInputFiles().stream()
                .filter(this::isValidInputFile)
                .toList();

        if (validFiles.isEmpty()) {
            log.error("Cannot continue: no readable input files were provided.");
            return;
        }

        fileProcessorService.processFiles(validFiles);
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