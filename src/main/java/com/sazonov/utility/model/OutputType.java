package com.sazonov.utility.model;

import lombok.Getter;

@Getter
public enum OutputType {
    INTEGER("integers.txt", "integer"),
    FLOAT("floats.txt", "float"),
    STRING("strings.txt", "string");

    private final String fileName;
    private final String label;

    OutputType(String fileName, String label) {
        this.fileName = fileName;
        this.label = label;
    }
}
