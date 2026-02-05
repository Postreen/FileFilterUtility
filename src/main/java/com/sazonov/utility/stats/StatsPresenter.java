package com.sazonov.utility.stats;

import com.sazonov.utility.model.StatisticsMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class StatsPresenter {
    private final StatisticsMode mode;

    public void print(StatsTracker stats) {
        if (mode == StatisticsMode.NONE) {
            log.info("Statistics are not printed because the mode is set to NONE.");
            return;
        }
        printNumeric("Integers", stats.getIntegerStats());
        printNumeric("Floats", stats.getFloatStats());
        printStrings(stats.getStringStats());
    }

    private void printNumeric(String title, NumericStats stats) {
        log.info("{}:", title);
        log.info("  count: {}", stats.getCount());
        if (mode == StatisticsMode.FULL && stats.getCount() > 0) {
            log.info("  min: {}", stats.getMin());
            log.info("  max: {}", stats.getMax());
            log.info("  sum: {}", stats.getSum());
            log.info("  average: {}", stats.average());
        } else if (mode == StatisticsMode.FULL) {
            log.info("  no data");
        }
    }

    private void printStrings(StringStats stats) {
        log.info("Strings:");
        log.info("  count: {}", stats.getCount());
        if (mode == StatisticsMode.FULL && stats.getCount() > 0) {
            log.info("  min length: {}", stats.getMinLength());
            log.info("  max length: {}", stats.getMaxLength());
        } else if (mode == StatisticsMode.FULL) {
            log.info("  no data");
        }
    }
}
