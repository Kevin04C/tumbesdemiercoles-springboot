package com.tumbesdemiercoles.api.access.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para actualizar excepciones de permisos de un usuario.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPermissionExceptionsRequestDto {

  private List<PermissionOverrideDto> overrides;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PermissionOverrideDto {
    private UUID permissionId;
    private Boolean isActive;
  }

}
