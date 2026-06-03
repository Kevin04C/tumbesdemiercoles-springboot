package com.tumbesdemiercoles.api.auth.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

  private String firstName;

  private String lastName;

  @Email(message = "Email inválido")
  @NotBlank(message = "El email es obligatorio")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
  private String password;

  private String imageUrl;

  private String userName;
}
