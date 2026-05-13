package com.tumbesdemiercoles.api.access.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "roleId", "permissionId", "statusRegistry"})
public class RolePermissionResponse {
  private UUID id;
  private UUID roleId;
  private UUID permissionId;
  private String statusRegistry;
}
