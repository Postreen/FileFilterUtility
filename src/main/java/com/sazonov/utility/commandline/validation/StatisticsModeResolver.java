package com.sazonov.utility.commandline.validation;

import com.sazonov.utility.model.StatisticsMode;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Component;

@Component
public class StatisticsModeResolver {
    public StatisticsMode resolveStatisticsMode(CommandLine cmd) {
        if (cmd.hasOption("s") && cmd.hasOption("f")) {
            throw new IllegalArgumentException("Both -s and -f options cannot be selected simultaneously.");
        }

        if (cmd.hasOption("f")) {
            return StatisticsMode.FULL;
        } else if (cmd.hasOption("s")) {
            return StatisticsMode.SUMMARY;
        } else {
            return StatisticsMode.NONE;
        }
    }
}
