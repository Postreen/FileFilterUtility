package com.sazonov.utility.commandline;

import com.sazonov.utility.commandline.validation.DirectoryValidator;
import com.sazonov.utility.commandline.validation.FileValidator;
import com.sazonov.utility.commandline.validation.PrefixValidator;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import com.sazonov.utility.utils.HelpPrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CliParser {
    private final CliOptionsFactory cliOptionsFactory;
    private final Configuration configuration;
    private final DirectoryValidator directoryValidator;
    private final FileValidator fileValidator;
    private final PrefixValidator prefixValidator;

    public void parse(String[] args) throws ParseException {
        Locale.setDefault(Locale.ROOT);
        CommandLineParser parser = new DefaultParser();


        try {
            CommandLine cmd = parser.parse(cliOptionsFactory.createOptions(), args);

            Path outputDirectory = directoryValidator.ensureOutputDirectory(cmd.getOptionValue("o", "."));

            String prefix = cmd.getOptionValue("p", "");
            if (cmd.hasOption("p")) {
                prefixValidator.validatePrefix(prefix);  // Валидация префикса через PrefixValidator
            }

            boolean append = cmd.hasOption("a");
            StatisticsMode statisticsMode = resolveStatisticsMode(cmd);

            List<Path> inputs = fileValidator.validateAndFilterFiles(cmd.getArgList());

            configuration.updateConfiguration(outputDirectory, prefix, append, statisticsMode, inputs);

            logConfigurationDetails(configuration);
        } catch (MissingArgumentException e) {
            if (e.getOption() != null && "p".equals(e.getOption().getOpt())) {
                log.error("Prefix option (-p) requires a value.");
            } else {
                log.error("Incorrect command line format. Please refer to the help message.");
            }
            HelpPrinter.printHelp(cliOptionsFactory.createOptions());
            throw e;
        } catch (ParseException e) {
            log.error("Incorrect command line format. Please refer to the help message.");
            HelpPrinter.printHelp(cliOptionsFactory.createOptions());
            throw e;
        } catch (Exception e) {
            HelpPrinter.printHelp(cliOptionsFactory.createOptions());
            throw e;
        }
    }

    private StatisticsMode resolveStatisticsMode(CommandLine cmd) {
        if (cmd.hasOption("s") && cmd.hasOption("f")) {
            log.error("Invalid combination of statistics options: both -s and -f cannot be selected simultaneously.");
            throw new IllegalArgumentException("Invalid combination of statistics options.");
        }
        if (cmd.hasOption("f")) {
            log.info("Full statistics requested.");
            return StatisticsMode.FULL;
        } else if (cmd.hasOption("s")) {
            log.info("Summary statistics requested.");
            return StatisticsMode.SUMMARY;
        } else {
            log.debug("No statistics mode selected.");
            return StatisticsMode.NONE;
        }
    }

    private void logConfigurationDetails(Configuration configuration) {
        log.debug("""
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
