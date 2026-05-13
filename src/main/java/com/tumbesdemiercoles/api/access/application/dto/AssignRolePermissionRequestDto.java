package com.tumbesdemiercoles.api.access.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para asignar permisos a un rol.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolePermissionRequestDto {

  private UUID roleId;
  private List<UUID> permissionIds;

}
