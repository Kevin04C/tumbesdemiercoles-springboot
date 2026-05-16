package com.tumbesdemiercoles.api.access.domain.exception;

import java.util.UUID;

/**
 * Excepción de dominio lanzada cuando un rol no es encontrado.
 */
public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException(UUID id) {
    super("Rol con ID " + id + " no encontrado");
  }

  public RoleNotFoundException(String message) {
    super(message);
  }

}
