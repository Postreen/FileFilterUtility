package com.sazonov.utility.commandline.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CliOptionsValidator {

    private static final Pattern PATH_PATTERN = Pattern.compile(".*[/.].*");

    public String[] filterUnknownOptions(String[] args, Options options) {
        List<String> filteredArgs = new ArrayList<>();

        boolean isFileMode = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (!arg.startsWith("-") && !arg.equals("-") && isPath(arg)) {
                filteredArgs.add(arg);
                isFileMode = true;
                continue;
            }

            String normalizedOption = normalizeOptionName(arg);
            if (normalizedOption != null && isKnownOption(normalizedOption, options)) {
                filteredArgs.add(arg);
                isFileMode = false;
                continue;
            }

            log.warn("Unknown CLI parameter '{}' is ignored.", arg);

            if (isFileMode && shouldSkipPotentialValue(arg, i, args)) {
                log.warn("Value '{}' for unknown CLI parameter '{}' is ignored.", args[i + 1], arg);
                i++;
            }
        }
        return filteredArgs.toArray(new String[0]);
    }

    private boolean isPath(String arg) {
        return PATH_PATTERN.matcher(arg).matches();
    }

    private String normalizeOptionName(String arg) {
        if (arg.startsWith("--")) {
            String optionName = arg.substring(2);
            int separatorIndex = optionName.indexOf('=');
            return separatorIndex >= 0 ? optionName.substring(0, separatorIndex) : optionName;
        }

        if (arg.length() == 2) {
            return String.valueOf(arg.charAt(1));
        }

        if (arg.length() > 2) {
            return String.valueOf(arg.charAt(1));
        }

        return null;
    }

    private boolean isKnownOption(String optionName, Options options) {
        return options.hasOption(optionName) || options.hasLongOption(optionName);
    }

    private boolean shouldSkipPotentialValue(String arg, int currentIndex, String[] args) {
        return !arg.contains("=")
                && currentIndex + 1 < args.length
                && !args[currentIndex + 1].startsWith("-");
    }
}