package com.sazonov.utility.stats;

import com.sazonov.utility.model.StatisticsMode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class StatsPresenter {
    private final StatisticsMode mode;

    public void print(StatsTracker stats) {
        if (mode == StatisticsMode.NONE) {
            return;
        }
        printNumeric("Integers", stats.getIntegerStats());
        printNumeric("Floats", stats.getFloatStats());
        printStrings(stats.getStringStats());
    }

    private void printNumeric(String title, NumericStats stats) {
        System.out.println(title + ":");
        System.out.println("  count: " + stats.getCount());
        if (mode == StatisticsMode.FULL && stats.getCount() > 0) {
            System.out.println("  min: " + stats.getMin());
            System.out.println("  max: " + stats.getMax());
            System.out.println("  sum: " + stats.getSum());
            System.out.println("  average: " + stats.average());
        } else if (mode == StatisticsMode.FULL) {
            System.out.println("  no data");
        }
    }

    private void printStrings(StringStats stats) {
        System.out.println("Strings:");
        System.out.println("  count: " + stats.getCount());
        if (mode == StatisticsMode.FULL && stats.getCount() > 0) {
            System.out.println("  min length: " + stats.getMinLength());
            System.out.println("  max length: " + stats.getMaxLength());
        } else if (mode == StatisticsMode.FULL) {
            System.out.println("  no data");
        }
    }
}