package com.sazonov.utility.cli;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class CliParser {
    private static final String OPTION_OUTPUT = "o";
    private static final String OPTION_PREFIX = "p";
    private static final String OPTION_APPEND = "a";
    private static final String OPTION_SUMMARY = "s";
    private static final String OPTION_FULL = "f";

    private final Options options;

    public CliParser() {
        options = new Options();
        options.addOption(Option.builder(OPTION_OUTPUT)
                .longOpt("output")
                .hasArg()
                .argName("dir")
                .desc("Output directory for result files (default: current directory)")
                .build());
        options.addOption(Option.builder(OPTION_PREFIX)
                .longOpt("prefix")
                .hasArg()
                .argName("value")
                .desc("Prefix for output file names")
                .build());
        options.addOption(Option.builder(OPTION_APPEND)
                .longOpt("append")
                .desc("Append to existing output files")
                .build());
        options.addOption(Option.builder(OPTION_SUMMARY)
                .longOpt("summary")
                .desc("Print summary statistics")
                .build());
        options.addOption(Option.builder(OPTION_FULL)
                .longOpt("full")
                .desc("Print full statistics")
                .build());
    }

    public Optional<Configuration> parse(String[] args) {
        Locale.setDefault(Locale.ROOT);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            log.debug("Parsing completed successfully.");

            Path outputDirectory = Paths.get(cmd.getOptionValue(OPTION_OUTPUT, "."));
            log.debug("Output directory: {}", outputDirectory);

            String prefix = cmd.getOptionValue(OPTION_PREFIX, "");
            log.debug("Output prefix: {}", prefix);

            boolean append = cmd.hasOption(OPTION_APPEND);
            log.debug("Append option: {}", append);

            StatisticsMode statisticsMode = resolveStatisticsMode(cmd);
            log.info("Statistics mode: {}", statisticsMode);

            List<Path> inputs = new ArrayList<>();
            for (String input : cmd.getArgList()) {
                inputs.add(Paths.get(input));
            }
            return Optional.of(new Configuration(outputDirectory, prefix, append, statisticsMode, inputs));
        } catch (ParseException e) {
            log.warn("Failed to parse arguments: {}", e.getMessage());
            printHelp();
            return Optional.empty();
        }
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <jar> [options] <files...>", options);
        log.debug("Help printed to console.");
    }

    private StatisticsMode resolveStatisticsMode(CommandLine cmd) {
        if (cmd.hasOption(OPTION_FULL)) {
            log.info("Full statistics requested.");
            return StatisticsMode.FULL;
        } else if (cmd.hasOption(OPTION_SUMMARY)) {
            log.info("Summary statistics requested.");
            return StatisticsMode.SUMMARY;
        } else {
            log.info("No statistics mode selected.");
            return StatisticsMode.NONE;
        }
    }
}
