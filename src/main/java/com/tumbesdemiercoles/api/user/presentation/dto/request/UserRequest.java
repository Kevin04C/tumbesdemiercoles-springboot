package com.tumbesdemiercoles.api.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest { // Capa de Presentación

  @NotBlank(message = "El nombre no puede estar vacío")
  private String firstName;

  @NotBlank(message = "El apellido es obligatorio")
  private String lastName;

  @Email(message = "Email inválido")
  private String email;

  @NotBlank
  private String password;

  private String imageUrl;
}
