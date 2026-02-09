package com.sazonov.utility.commandline.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PrefixValidator {

    private static final Pattern PREFIX_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

    /**
     * Проверяет, является ли префикс валидным.
     */
    public void validatePrefix(String prefix) {
        if (prefix == null || prefix.isBlank() || !isPrefixValid(prefix)) {
            log.error("Invalid prefix specified: {}. Prefix cannot be empty or contain invalid characters.", prefix);
            throw new IllegalArgumentException("Invalid prefix specified: " + prefix);
        }
    }

    /**
     * Проверка на валидность префикса по регулярному выражению и дополнительным ограничениям.
     */
    private boolean isPrefixValid(String prefix) {
        return PREFIX_PATTERN.matcher(prefix).matches()
                && !prefix.contains(File.separator)
                && !prefix.contains("/");
    }
}
