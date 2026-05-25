package com.tumbesdemiercoles.api.access.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateUserPermissionExceptionsRequest {

  @NotEmpty(message = "Debe proporcionar al menos un override de permiso")
  @Valid
  private List<PermissionOverrideRequest> overrides;

  @Data
  public static class PermissionOverrideRequest {

    @NotNull(message = "El id del permiso es requerido")
    private UUID permissionId;

    @NotNull(message = "El estado isActive es requerido")
    private Boolean isActive;

  }

}
