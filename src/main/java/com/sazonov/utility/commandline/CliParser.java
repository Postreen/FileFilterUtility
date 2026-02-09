package com.sazonov.utility.commandline;

import com.sazonov.utility.commandline.validation.DirectoryValidator;
import com.sazonov.utility.commandline.validation.FileValidator;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import com.sazonov.utility.utils.HelpPrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CliParser {
    private final CliOptionsFactory cliOptionsFactory;
    private final Configuration configuration;
    private final DirectoryValidator directoryValidator;
    private final FileValidator fileValidator;

    public void parse(String[] args) throws ParseException {
        Locale.setDefault(Locale.ROOT);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(cliOptionsFactory.createOptions(), args);

            Path outputDirectory = directoryValidator.ensureOutputDirectory(cmd.getOptionValue("o", "."));

            String prefix = cmd.getOptionValue("p", "");
            boolean append = cmd.hasOption("a");
            StatisticsMode statisticsMode = resolveStatisticsMode(cmd);

            List<Path> inputs = fileValidator.validateAndFilterFiles(cmd.getArgList());

            configuration.updateConfiguration(outputDirectory, prefix, append, statisticsMode, inputs);

            logConfigurationDetails(configuration);

        } catch (Exception e) {
            HelpPrinter.printHelp(cliOptionsFactory.createOptions());
            throw e;
        }
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

    private void logConfigurationDetails(Configuration configuration) {
        log.info("""
                        Configuration details:
                        - Output directory: {}
                        - Output prefix: {}
                        - Append option: {}
                        - Statistics mode: {}
                        - Input files: {}
                        """,
                configuration.getOutputDirectory(),
                configuration.getPrefix(),
                configuration.getAppend(),
                configuration.getStatisticsMode(),
                configuration.getInputFiles()
        );
    }
}