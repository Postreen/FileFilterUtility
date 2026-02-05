package com.sazonov.utility.stats;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

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
            System.err.println(e);
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
            System.err.println(e);
        }
    }

    /**
     * Метод для записи строк
     */
    public void recordString(String value) {
        stringStats.record(value);
    }
}
