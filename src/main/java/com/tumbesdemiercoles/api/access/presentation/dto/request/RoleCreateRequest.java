package com.tumbesdemiercoles.api.access.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleCreateRequest {

  @NotBlank(message = "El nombre del rol es obligatorio")
  @Size(max = 32, message = "El nombre del rol no puede exceder los 32 caracteres")
  private String name;

}
