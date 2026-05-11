package com.tumbesdemiercoles.api.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

  @Size(max = 50, message = "El Nombre no puede exceder los 50 caracteres")
  private String firstName;

  @Size(max = 50, message = "El Apellido no puede exceder los 50 caracteres")
  private String lastName;

  @Email(message = "El formato del correo no es válido")
  private String email;

  private String imageUrl;
}
