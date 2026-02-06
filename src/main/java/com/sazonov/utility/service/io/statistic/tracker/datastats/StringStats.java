package com.sazonov.utility.service.io.statistic.tracker.datastats;

import lombok.Getter;

@Getter
public final class StringStats {
    private long count;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    public void record(String value) {
        int length = value.length();
        count++;

        if (length < minLength) {
            minLength = length;
        }

        if (length > maxLength) {
            maxLength = length;
        }
    }
}
