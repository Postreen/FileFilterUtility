package com.sazonov.utility.service;

import com.sazonov.utility.commandline.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.manager.FileOutputManager;
import com.sazonov.utility.processor.FileFilterProcessor;
import com.sazonov.utility.stats.StatsPresenter;
import com.sazonov.utility.stats.StatsTracker;

import java.util.Optional;

public class ServiceFactory {
    public static FileProcessingService createFileProcessingService(String[] args) {
        CliParser cliParser = new CliParser();
        Optional<Configuration> configOptional = cliParser.parse(args);
        if (configOptional.isEmpty()) {
            return null;
        }
        Configuration config = configOptional.get();

        FileOutputManager outputManager = new FileOutputManager(config);
        StatsTracker statsTracker = new StatsTracker();
        FileFilterProcessor processor = new FileFilterProcessor(outputManager, statsTracker);
        StatsPresenter presenter = new StatsPresenter(config.statisticsMode());

        return new FileProcessingService(cliParser, outputManager, statsTracker, processor, presenter);
    }
}
