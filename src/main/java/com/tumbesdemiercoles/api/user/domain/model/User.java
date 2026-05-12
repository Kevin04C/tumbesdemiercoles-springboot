package com.tumbesdemiercoles.api.user.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.Auditable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
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
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Protegido para frameworks
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends Auditable {

  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private Boolean isEmailVerified;
  private String passwordHash;
  private Boolean isActive;

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
        .isEmailVerified(false)
        .statusRegistry("ACTIVE")
        .isActive(true)
        .build();
  }

  public void verifyEmail() {
    if (this.isEmailVerified) {
      throw new IllegalStateException("El usuario ya tiene el correo verificado");
    }
    this.isEmailVerified = true;
  }

  public void updateProfile(String newFirstName, String newLastName, String newImageUrl) {
    if (newFirstName == null || newFirstName.isBlank()) {
      throw new IllegalArgumentException("El nombre no puede estar vacío");
    }
    this.firstName = newFirstName;
    this.lastName = newLastName;
    if (newImageUrl != null && !newImageUrl.isBlank()) {
      this.imageUrl = newImageUrl;
    }
  }

  public void deleteAccount() {
    if ("DELETE".equals(this.getStatusRegistry())) {
      return;
    }
    this.setStatusRegistry("DELETE");
    this.isActive = false;
  }

  public void changePassword(String newPasswordHash) {
    if (!this.getStatusRegistry().equals("ACTIVE")) {
      throw new IllegalStateException("No se puede cambiar la contraseña de un usuario inactivo");
    }
    this.passwordHash = newPasswordHash;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

}
