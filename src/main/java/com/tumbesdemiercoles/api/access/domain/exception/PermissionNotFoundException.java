package com.tumbesdemiercoles.api.access.domain.exception;

import java.util.UUID;

/**
 * Excepción de dominio lanzada cuando un permiso no es encontrado.
 */
public class PermissionNotFoundException extends RuntimeException {

  public PermissionNotFoundException(UUID id) {
    super("Permiso con ID " + id + " no encontrado");
  }

  public PermissionNotFoundException(String message) {
    super(message);
  }

}
