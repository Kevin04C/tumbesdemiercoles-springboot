package com.tumbesdemiercoles.api.user.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.Auditable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio User.
 * POJO puro sin anotaciones de frameworks (ni Spring Data, ni JPA).
 * Representa el concepto de negocio "Usuario" en su forma más pura.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable {

  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private Boolean emailVerified;
  private String passwordHash;

  public static User createNewUser(String firstName, String lastName, String email, String passwordHash, String imageUrl) {
    String finalImageUrl = (imageUrl == null || imageUrl.isBlank())
        ? "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg"
        : imageUrl;
    return User.builder()
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .passwordHash(passwordHash)
        .imageUrl(finalImageUrl)
        .emailVerified(false)
        .statusRegistry("ACTIVE")
        .build();
  }

}
