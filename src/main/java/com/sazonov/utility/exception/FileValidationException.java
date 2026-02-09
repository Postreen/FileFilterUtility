package com.sazonov.utility.exception;

import java.time.Instant;

public class FileValidationException extends RuntimeException {
  private final Instant timestamp;
  private final String details;

  public FileValidationException(String details) {
    super("Invalid file input");
    this.timestamp = Instant.now();
    this.details = details;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + ". " + details + " " + timestamp.toString();
  }
}
