package com.tumbesdemiercoles.api.access.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "name", "statusRegistry"})
public class RoleResponse {
  private UUID id;
  private String name;
  private String statusRegistry;
}
