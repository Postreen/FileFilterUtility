package com.sazonov.utility.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutputType {
    INTEGER("integers.txt", "integer"),
    FLOAT("floats.txt", "float"),
    STRING("strings.txt", "string");

    private final String fileName;
    private final String label;
}
