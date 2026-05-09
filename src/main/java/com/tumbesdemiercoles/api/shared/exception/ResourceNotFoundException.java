package com.tumbesdemiercoles.api.shared.exception;

import lombok.Getter;

/**
 * Excepción para recursos no encontrados (404).
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

  private final String details;

  public ResourceNotFoundException(String message) {
    super(message);
    this.details = null;
  }

  public ResourceNotFoundException(String message, String details) {
    super(message);
    this.details = details;
  }

  public static ResourceNotFoundException forEntity(String entityName, Object id) {
    return new ResourceNotFoundException(
        entityName + " not found",
        "No " + entityName.toLowerCase() + " exists with id: " + id
    );
  }

}
