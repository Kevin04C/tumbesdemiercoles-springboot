package com.tumbesdemiercoles.api.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de dominio User.
 * POJO puro sin anotaciones de frameworks (ni Spring Data, ni JPA).
 * Representa el concepto de negocio "Usuario" en su forma más pura.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private UUID userId;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private Boolean emailVerified;
  private String passwordHash;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String statusRegistry;
  private LocalDateTime statusUpdatedAt;

}
