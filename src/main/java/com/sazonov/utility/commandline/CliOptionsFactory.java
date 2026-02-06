package com.sazonov.utility.commandline;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CliOptionsFactory {
    public static Options createOptions() {
        Options options = new Options();

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

        return options;
    }
}
