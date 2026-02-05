package com.sazonov.utility.service;

import com.sazonov.utility.commandline.CliParser;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FileProcessingService {

    private final CliParser cliParser;
    private final FileOutputManager outputManager;
    private final StatsTracker statsTracker;
    private final FileFilterProcessor processor;
    private final StatsPresenter presenter;

    public void process(String[] args) {
        log.info("Starting FileFilterUtility");

        Optional<Configuration> configOptional = cliParser.parse(args);
        if (configOptional.isEmpty()) {
            return;
        }

        Configuration config = configOptional.get();

        // Проверка наличия входных файлов
        if (config.inputFiles().isEmpty()) {
            log.error("No input files provided.");
            cliParser.printHelp();
            return;
        }

        // Создание каталога для сохранения результатов
        if (!ensureOutputDirectory(config.outputDirectory())) {
            log.error("Failed to create the output directory.");
            return;
        }

        // Обработка файлов
        processFiles(config);
    }

    private boolean ensureOutputDirectory(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            log.debug("Output directory created or already exists: {}", outputDirectory);
            return true;
        } catch (IOException e) {
            log.error("Failed to create output directory: {}", e.getMessage());
            return false;
        }
    }

    private void processFiles(Configuration config) {
        log.info("Starting file processing...");
        processor.process(config.inputFiles());
        outputManager.closeAll();
        log.info("File processing completed.");

        presenter.print(statsTracker);
    }
}