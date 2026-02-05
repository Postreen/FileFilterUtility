package com.sazonov.utility;

import com.sazonov.utility.cli.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.manager.FileOutputManager;
import com.sazonov.utility.processor.FileFilterProcessor;
import com.sazonov.utility.stats.StatsPresenter;
import com.sazonov.utility.stats.StatsTracker;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Starting FileFilterUtility");
        log.debug("Arguments: {}", String.join(", ", args));

        CliParser cliParser = new CliParser();

        Optional<Configuration> configOptional = cliParser.parse(args);
        if (configOptional.isEmpty()) {
            log.warn("Failed to parse arguments.");
            return;
        }

        Configuration config = configOptional.get();
        if (config.inputFiles().isEmpty()) {
            log.error("No input files provided.");
            cliParser.printHelp();
            return;
        }

        if (!ensureOutputDirectory(config.outputDirectory())) {
            log.error("Failed to create the output directory.");
            return;
        }

        FileOutputManager outputManager = new FileOutputManager(config);
        StatsTracker statsTracker = new StatsTracker();
        FileFilterProcessor processor = new FileFilterProcessor(outputManager, statsTracker);

        log.info("Starting file processing...");
        processor.process(config.inputFiles());
        outputManager.closeAll();
        log.info("File processing completed.");

        StatsPresenter presenter = new StatsPresenter(config.statisticsMode());
        presenter.print(statsTracker);
    }

    private static boolean ensureOutputDirectory(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            log.debug("Output directory created or already exists: {}", outputDirectory);
            return true;
        } catch (IOException e) {
            log.error("Failed to create output directory: {}", e.getMessage());
            return false;
        }
    }
}