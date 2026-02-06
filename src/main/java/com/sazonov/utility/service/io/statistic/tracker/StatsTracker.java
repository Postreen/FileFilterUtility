package com.sazonov.utility.service.io.statistic.tracker;

import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Getter
public final class StatsTracker {
    private final NumericStats integerStats = new NumericStats();
    private final NumericStats floatStats = new NumericStats();
    private final StringStats stringStats = new StringStats();

    /**
     * Метод для записи целых чисел
     */
    public void recordInteger(String value) {
        try {
            BigInteger parsed = new BigInteger(value);
            integerStats.record(new BigDecimal(parsed));
        } catch (NumberFormatException e) {
            log.warn("Failed to parse integer value '{}': {}", value, e.getMessage());
        }
    }

    /**
     * Метод для записи чисел с плавающей точкой
     */
    public void recordFloat(String value) {
        try {
            BigDecimal parsed = new BigDecimal(value);
            floatStats.record(parsed);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse float value '{}': {}", value, e.getMessage());
        }
    }

    /**
     * Метод для записи строк
     */
    public void recordString(String value) {
        stringStats.record(value);
    }
}
