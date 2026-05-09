package com.tumbesdemiercoles.api.shared.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
  private final String details;

  public ConflictException(String message) {
    super(message);
    this.details = null;
  }

  public ConflictException(String message, String details) {
    super(message);
    this.details = details;
  }

  public static ConflictException forDuplicate(String entityName, String field, Object value) {
    return new ConflictException(
        "Conflict creating " + entityName,
        "A " + entityName.toLowerCase() + " already exists with " + field + ": " + value
    );
  }

}
