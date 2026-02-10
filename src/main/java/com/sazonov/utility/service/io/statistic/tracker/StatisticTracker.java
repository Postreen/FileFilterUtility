package com.sazonov.utility.service.io.statistic.tracker;

import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public final class StatisticTracker implements StatisticProvider {
    private final NumericStats integerStats;
    private final NumericStats floatStats;
    private final StringStats stringStats;

    public void recordInteger(String value) {
        try {
            BigInteger parsed = new BigInteger(value);
            integerStats.record(new BigDecimal(parsed));
        } catch (NumberFormatException e) {
            log.warn("Failed to parse integer value '{}': {}", value, e.getMessage());
        }
    }

    public void recordFloat(String value) {
        try {
            BigDecimal parsed = new BigDecimal(value);
            floatStats.record(parsed);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse float value '{}': {}", value, e.getMessage());
        }
    }

    public void recordString(String value) {
        stringStats.record(value);
    }
}
