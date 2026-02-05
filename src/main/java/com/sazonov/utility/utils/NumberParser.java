package com.sazonov.utility.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class NumberParser {
    private static final String INTEGER_REGEX = "[+-]?\\d+";
    private static final String FLOAT_REGEX = "[+-]?(?:\\d+\\.?\\d*|\\d*\\.?\\d+)(?:[eE][+-]?\\d+)?";

    public static boolean isInteger(String value) {
        return value.matches(INTEGER_REGEX);
    }

    public static boolean isFloat(String value) {
        if (!value.matches(FLOAT_REGEX)) {
            return false;
        }
        return value.contains(".") || value.contains("e") || value.contains("E");
    }
}
