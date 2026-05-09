package com.tumbesdemiercoles.api.shared.exception;

import lombok.Getter;

/**
 * Excepción para solicitudes inválidas (400).
 */
@Getter
public class BadRequestException extends RuntimeException {

  private final String details;

  public BadRequestException(String message) {
    super(message);
    this.details = null;
  }

  public BadRequestException(String message, String details) {
    super(message);
    this.details = details;
  }

}
