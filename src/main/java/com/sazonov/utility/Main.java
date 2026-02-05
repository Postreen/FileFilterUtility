package com.sazonov.utility;

import com.sazonov.utility.cli.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.manager.FileOutputManager;
import com.sazonov.utility.processor.FileFilterProcessor;
import com.sazonov.utility.stats.StatsPresenter;
import com.sazonov.utility.stats.StatsTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        CliParser cliParser = new CliParser();
        Optional<Configuration> configOptional = cliParser.parse(args);
        if (configOptional.isEmpty()) {
            return;
        }

        Configuration config = configOptional.get();
        if (config.inputFiles().isEmpty()) {
            cliParser.printHelp();
            return;
        }

        if (!ensureOutputDirectory(config.outputDirectory())) {
            return;
        }

        FileOutputManager outputManager = new FileOutputManager(config);
        StatsTracker statsTracker = new StatsTracker();
        FileFilterProcessor processor = new FileFilterProcessor(outputManager, statsTracker);
        processor.process(config.inputFiles());
        outputManager.closeAll();

        StatsPresenter presenter = new StatsPresenter(config.statisticsMode());
        presenter.print(statsTracker);
    }

    private static boolean ensureOutputDirectory(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
}