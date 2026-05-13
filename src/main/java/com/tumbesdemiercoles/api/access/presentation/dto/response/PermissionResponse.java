package com.tumbesdemiercoles.api.access.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "name", "description", "statusRegistry"})
public class PermissionResponse {
  private UUID id;
  private String name;
  private String description;
  private String statusRegistry;
}
