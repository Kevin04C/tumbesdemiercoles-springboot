package com.tumbesdemiercoles.api.shared.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
  private final String details;

  public ForbiddenException(String message) {
    super(message);
    this.details = null;
  }

  public ForbiddenException(String message, String details) {
    super(message);
    this.details = details;
  }

  public static ForbiddenException forMissingRole(String requiredRole) {
    return new ForbiddenException(
        "Access denied",
        "This action requires the '" + requiredRole + "' role."
    );
  }
}
