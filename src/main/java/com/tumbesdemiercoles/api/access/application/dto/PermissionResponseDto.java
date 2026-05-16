package com.tumbesdemiercoles.api.access.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para operaciones de Permission.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDto {

  private UUID id;
  private String name;
  private String description;
  private String statusRegistry;

}
