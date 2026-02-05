package com.sazonov.utility.config;

import com.sazonov.utility.model.StatisticsMode;

import java.nio.file.Path;
import java.util.List;

public record Configuration(
        Path outputDirectory,
        String prefix,
        boolean append,
        StatisticsMode statisticsMode,
        List<Path> inputFiles
) {
}