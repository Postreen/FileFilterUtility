package com.sazonov.utility.commandline;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CliOptionsFactory {
    public Options createOptions() {
        Options options = new Options();

        try {
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
        } catch (Exception e) {
            log.error("Error creating CLI options: {}", e.getMessage());
            throw new RuntimeException("Error creating CLI options", e);
        }
        return options;
    }
}
