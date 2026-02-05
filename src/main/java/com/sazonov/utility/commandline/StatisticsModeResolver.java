package com.sazonov.utility.commandline;

import com.sazonov.utility.model.StatisticsMode;
import org.apache.commons.cli.CommandLine;

public class StatisticsModeResolver {
    public StatisticsMode resolveStatisticsMode(CommandLine cmd) {
        if (cmd.hasOption("f")) {
            return StatisticsMode.FULL;
        } else if (cmd.hasOption("s")) {
            return StatisticsMode.SUMMARY;
        } else {
            return StatisticsMode.NONE;
        }
    }
}