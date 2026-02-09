package com.sazonov.utility.config;

import com.sazonov.utility.model.StatisticsMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
public class Configuration {

    private Path outputDirectory;
    private String prefix;
    private Boolean append;
    private StatisticsMode statisticsMode;
    private List<Path> inputFiles;

    public void updateConfiguration(
            Path outputDirectory,
            String prefix,
            Boolean append,
            StatisticsMode statisticsMode,
            List<Path> inputFiles
    ) {
        this.outputDirectory = outputDirectory;
        this.prefix = prefix;
        this.append = append;
        this.statisticsMode = statisticsMode;
        this.inputFiles = inputFiles;
    }
}