package com.sazonov.utility.commandline;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ArgumentParser {
    private final Options options;

    public ArgumentParser() {
        options = new Options();
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("dir")
                .desc("Output directory for result files (default: current directory)")
                .build());
        options.addOption(Option.builder("p")
                .longOpt("prefix")
                .hasArg()
                .argName("value")
                .desc("Prefix for output file names")
                .build());
        options.addOption(Option.builder("a")
                .longOpt("append")
                .desc("Append to existing output files")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("summary")
                .desc("Print summary statistics")
                .build());
        options.addOption(Option.builder("f")
                .longOpt("full")
                .desc("Print full statistics")
                .build());
    }

    public CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    public Configuration parseConfiguration(CommandLine cmd) {
        Path outputDirectory = Paths.get(cmd.getOptionValue("o", "."));
        String prefix = cmd.getOptionValue("p", "");
        boolean append = cmd.hasOption("a");
        StatisticsMode statisticsMode = resolveStatisticsMode(cmd);
        List<Path> inputs = new ArrayList<>();
        for (String input : cmd.getArgList()) {
            inputs.add(Paths.get(input));
        }
        return new Configuration(outputDirectory, prefix, append, statisticsMode, inputs);
    }

    private StatisticsMode resolveStatisticsMode(CommandLine cmd) {
        if (cmd.hasOption("f")) {
            return StatisticsMode.FULL;
        } else if (cmd.hasOption("s")) {
            return StatisticsMode.SUMMARY;
        } else {
            return StatisticsMode.NONE;
        }
    }
}