package com.sazonov.utility.service.io.statistic;

import com.sazonov.utility.model.StatisticsMode;
import com.sazonov.utility.service.io.statistic.tracker.StatsTracker;
import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class StatisticReportService {
    private final StatisticsMode statisticsMode;

    /**
     * Печатает статистику
     */
    public void print(StatsTracker statsTracker) {
        if (statisticsMode == StatisticsMode.NONE) {
            log.info("Statistics are not printed because the mode is set to NONE.");
            return;
        }

        if (statisticsMode == StatisticsMode.FULL) {
            printFullStats("Integers", statsTracker.getIntegerStats());
            printFullStats("Floats", statsTracker.getFloatStats());
            printFullStringStats("Strings", statsTracker.getStringStats());
        } else if (statisticsMode == StatisticsMode.SUMMARY) {
            printSummaryStats("Integers", statsTracker.getIntegerStats());
            printSummaryStats("Floats", statsTracker.getFloatStats());
            printSummaryStringStats("Strings", statsTracker.getStringStats());
        }
    }

    /**
     * Полная статистика для чисел
     */
    private void printFullStats(String label, NumericStats stats) {
        log.info("""
                {}:
                - Min: {}
                - Max: {}
                - Average: {}
                - Sum: {}
                - Count: {}
                """,
                label,
                formatNullable(stats.getMin()),
                formatNullable(stats.getMax()),
                stats.average(),
                stats.getSum(),
                stats.getCount()
        );
    }

    /**
     * Краткая статистика для чисел
     */
    private void printSummaryStats(String label, NumericStats stats) {
        log.info("{}:", label);
        log.info("- Count: {}", stats.getCount());
    }

    /**
     * Полная статистика для строк
     */
    private void printFullStringStats(String label, StringStats stats) {
        log.info("""
                {}:
                - Min length: {}
                - Max length: {}
                - Count: {}
                """,
                label,
                stats.getCount() == 0 ? "N/A" : stats.getMinLength(),
                stats.getCount() == 0 ? "N/A" : stats.getMaxLength(),
                stats.getCount()
        );
    }

    /**
     * Краткая статистика для строк
     */
    private void printSummaryStringStats(String label, StringStats stats) {
        log.info("{}:", label);
        log.info("- Count: {}", stats.getCount());
    }

    private String formatNullable(Object value) {
        return value == null ? "N/A" : value.toString();
    }
}