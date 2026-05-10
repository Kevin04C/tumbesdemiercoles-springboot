package com.tumbesdemiercoles.api.shared.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
  private final String details;

  public UnauthorizedException(String message) {
    super(message);
    this.details = null;
  }

  public UnauthorizedException(String message, String details) {
    super(message);
    this.details = details;
  }

  public static UnauthorizedException forInvalidCredentials() {
    return new UnauthorizedException(
        "Authentication failed",
        "Invalid email or password."
    );
  }

  public static UnauthorizedException forInvalidToken() {
    return new UnauthorizedException(
        "Invalid token",
        "The provided JWT token is expired, missing, or malformed."
    );
  }
}
