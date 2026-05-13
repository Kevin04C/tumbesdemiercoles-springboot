package com.tumbesdemiercoles.api.access.presentation.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleUpdateRequest {

  @Size(max = 32, message = "El nombre del rol no puede exceder los 32 caracteres")
  private String name;

}
