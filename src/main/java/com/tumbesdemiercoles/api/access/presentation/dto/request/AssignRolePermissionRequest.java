package com.tumbesdemiercoles.api.access.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AssignRolePermissionRequest {

  @NotEmpty(message = "Debe proporcionar al menos un permiso")
  private List<UUID> permissionIds;

}
