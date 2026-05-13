package com.tumbesdemiercoles.api.access.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para la relación UserPermission.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionResponseDto {

  private UUID id;
  private UUID userId;
  private UUID permissionId;
  private Boolean isActive;
  private String statusRegistry;

}
