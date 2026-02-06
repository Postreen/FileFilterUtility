package com.sazonov.utility.service.io.statistic.tracker.datastats;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Getter
@Component
public final class NumericStats {
    private long count;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal sum = BigDecimal.ZERO;

    public void record(BigDecimal value) {
        count++;
        if (min == null || value.compareTo(min) < 0) {
            min = value;
        }
        if (max == null || value.compareTo(max) > 0) {
            max = value;
        }
        sum = sum.add(value);
    }

    public BigDecimal average() {
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
    }
}
