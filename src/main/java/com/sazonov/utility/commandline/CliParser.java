package com.sazonov.utility.commandline;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CliParser {
    private final CliOptionsFactory cliOptionsFactory;
    private final Configuration configuration;

    @PostConstruct
    public Optional<Configuration> parse(String[] args) {
        Locale.setDefault(Locale.ROOT);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(cliOptionsFactory.createOptions(), args);
            log.debug("Parsing completed successfully.");

            Path outputDirectory = Paths.get(cmd.getOptionValue("o", "."));
            log.debug("Output directory: {}", outputDirectory);

            String prefix = cmd.getOptionValue("p", "");
            log.debug("Output prefix: {}", prefix);

            boolean append = cmd.hasOption("a");
            log.debug("Append option: {}", append);

            StatisticsMode statisticsMode = resolveStatisticsMode(cmd);
            log.info("Statistics mode: {}", statisticsMode);

            List<Path> inputs = new ArrayList<>();
            for (String input : cmd.getArgList()) {
                inputs.add(Paths.get(input));
            }
            configuration.updateConfiguration(outputDirectory, prefix, append, statisticsMode, inputs);

            return Optional.of(configuration);
        } catch (ParseException e) {
            log.warn("Failed to parse arguments: {}", e.getMessage());
            printHelp();
            return Optional.empty();
        }
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <jar> [options] <files...>", cliOptionsFactory.createOptions());
    }

    private StatisticsMode resolveStatisticsMode(CommandLine cmd) {
        if (cmd.hasOption("f")) {
            log.info("Full statistics requested.");
            return StatisticsMode.FULL;
        } else if (cmd.hasOption("s")) {
            log.info("Summary statistics requested.");
            return StatisticsMode.SUMMARY;
        } else {
            log.info("No statistics mode selected.");
            return StatisticsMode.NONE;
        }
    }
}