package com.tumbesdemiercoles.api.access.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para asignar permisos a un usuario.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserPermissionRequestDto {

  private UUID userId;
  private List<UUID> permissionIds;

}
