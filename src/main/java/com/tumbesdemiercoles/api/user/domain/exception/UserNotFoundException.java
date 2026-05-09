package com.tumbesdemiercoles.api.user.domain.exception;

import java.util.UUID;

/**
 * Excepción de dominio lanzada cuando un usuario no es encontrado.
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(UUID id) {
    super("Usuario con ID " + id + " no encontrado");
  }

  public UserNotFoundException(String message) {
    super(message);
  }

}
