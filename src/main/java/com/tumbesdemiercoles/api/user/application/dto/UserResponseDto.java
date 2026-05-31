package com.tumbesdemiercoles.api.user.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para operaciones de usuario.
 * Transporta datos desde los casos de uso hacia la capa de presentación.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private String userName;
  private Boolean isEmailVerified;
  private Boolean isActive;

}
