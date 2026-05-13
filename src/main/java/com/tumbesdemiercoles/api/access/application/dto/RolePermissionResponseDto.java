package com.tumbesdemiercoles.api.access.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para la relación RolePermission.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionResponseDto {

  private UUID id;
  private UUID roleId;
  private UUID permissionId;
  private String statusRegistry;

}
