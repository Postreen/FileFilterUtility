package com.sazonov.utility.service.io.statistic;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticReportService {
    private final Configuration configuration;

    /**
     * Печатает статистику
     */
    public void print(StatisticTracker statisticTracker) {
        if (configuration.getStatisticsMode() == StatisticsMode.NONE) {
            log.info("Statistics are not printed because the mode is set to NONE.");
            return;
        }

        if (configuration.getStatisticsMode() == StatisticsMode.FULL) {
            printFullStats("Integers", statisticTracker.getIntegerStats());
            printFullStats("Floats", statisticTracker.getFloatStats());
            printFullStringStats("Strings", statisticTracker.getStringStats());
        } else if (configuration.getStatisticsMode() == StatisticsMode.SUMMARY) {
            printSummaryStats("Integers", statisticTracker.getIntegerStats());
            printSummaryStats("Floats", statisticTracker.getFloatStats());
            printSummaryStringStats("Strings", statisticTracker.getStringStats());
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