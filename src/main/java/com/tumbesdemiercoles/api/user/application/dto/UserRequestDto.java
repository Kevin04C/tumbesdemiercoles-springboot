package com.tumbesdemiercoles.api.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para operaciones de usuario.
 * Transporta datos desde la capa de presentación hacia los casos de uso.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String imageUrl;
  private String userName;

}
