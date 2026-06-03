package com.tumbesdemiercoles.api.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest { // Capa de Presentación

  private String firstName;

  private String lastName;

  @NotBlank(message = "El userName es obligatorio")
  private String userName;

  @NotBlank(message = "Email inválido")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank
  private String password;

  private String imageUrl;
}
