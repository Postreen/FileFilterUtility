package com.sazonov.utility.commandline.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CliOptionsValidator {
    private final PrefixValidator prefixValidator;

    public String[] filterUnknownOptions(String[] args, Options options) {
        List<String> validArgs = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (isKnownOption(arg, options)) {
                validArgs.add(arg);

                if ("-p".equals(arg)) {
                    i = handlePrefixOption(args, i, validArgs);
                } else if ("-o".equals(arg)) {
                    i = handleOutputOption(args, i, validArgs);
                }

            } else if (looksLikePath(arg)) {
                validArgs.add(arg);
            } else {
                log.warn("Unknown CLI option '{}'", arg);
            }
        }
        return validArgs.toArray(new String[0]);
    }

    private boolean isKnownOption(String optionName, Options options) {
        return options.hasOption(optionName) || options.hasLongOption(optionName);
    }

    private boolean looksLikePath(String arg) {
        return arg != null && (arg.contains("/") || arg.contains("\\"));
    }

    private int handlePrefixOption(String[] args, int index, List<String> validArgs) {
        if (index + 1 < args.length) {
            String prefixArg = args[index + 1];
            if (prefixValidator.validatePrefix(prefixArg)) {
                validArgs.add(prefixArg);
                return index + 1;
            } else {
                log.warn("Invalid prefix '{}' after -p option, ignored", prefixArg);
            }
        } else {
            log.warn("No prefix provided after -p option");
        }
        return index;
    }

    private int handleOutputOption(String[] args, int index, List<String> validArgs) {
        if (index + 1 < args.length && !args[index + 1].startsWith("-")) {
            String pathArg = args[index + 1];
            validArgs.add(pathArg);
            return index + 1;
        } else {
            validArgs.remove(validArgs.size() - 1);
            log.warn("No path provided after -o option");
        }
        return index;
    }

}