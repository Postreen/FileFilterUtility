package com.sazonov.utility.commandline;

import com.sazonov.utility.commandline.validation.*;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import com.sazonov.utility.utils.HelpPrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
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
    private final PrefixValidator prefixValidator;
    private final CliOptionsValidator cliOptionsValidator;
    private final StatisticsModeResolver statisticsModeResolver;

    public void parse(String[] args) throws ParseException {
        Locale.setDefault(Locale.ROOT);
        CommandLineParser parser = new DefaultParser();
        Options options = cliOptionsFactory.createOptions();
        String[] sanitizedArgs = cliOptionsValidator.filterUnknownOptions(args, options);

        try {
            CommandLine cmd = parser.parse(options, sanitizedArgs);

            Path outputDirectory = directoryValidator.ensureOutputDirectory(cmd.getOptionValue("o", "."));

            String prefix = cmd.getOptionValue("p", "");
            if (cmd.hasOption("p")) {
                prefixValidator.validatePrefix(prefix);
            }

            boolean append = cmd.hasOption("a");
            StatisticsMode statisticsMode = statisticsModeResolver.resolveStatisticsMode(cmd);

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

    private void logConfigurationDetails(Configuration configuration) {
        log.debug("Configuration details: {}", configuration);
    }
}
