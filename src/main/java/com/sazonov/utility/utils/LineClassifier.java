package com.sazonov.utility.utils;

import com.sazonov.utility.model.DataType;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class LineClassifier {
    public static DataType classify(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return DataType.STRING;
        }
        if (NumberParser.isInteger(trimmed)) {
            return DataType.INTEGER;
        }
        if (NumberParser.isFloat(trimmed)) {
            return DataType.FLOAT;
        }
        return DataType.STRING;
    }
}
