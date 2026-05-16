package com.tumbesdemiercoles.api.access.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "userId", "permissionId", "isActive", "statusRegistry"})
public class UserPermissionResponse {
  private UUID id;
  private UUID userId;
  private UUID permissionId;
  private Boolean isActive;
  private String statusRegistry;
}
