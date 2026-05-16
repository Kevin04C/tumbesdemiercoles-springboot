package com.tumbesdemiercoles.api.access.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionCreateRequest {

  @NotBlank(message = "El nombre del permiso es obligatorio")
  @Size(max = 100, message = "El nombre del permiso no puede exceder los 100 caracteres")
  private String name;

  @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
  private String description;

}
