package com.sazonov.utility.exception;

import java.time.Instant;

public class DirectoryCreationException extends RuntimeException {
    private final Instant timestamp;
    private final String details;

    public DirectoryCreationException(String details) {
        super("Unable to create output directory");
        this.timestamp = Instant.now();
        this.details = details;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ". " + details + " " + timestamp.toString();
    }
}