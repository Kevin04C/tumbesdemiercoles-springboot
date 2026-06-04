package com.tumbesdemiercoles.api.auth.application.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthUserDetailsDto {
  private UUID id;
  private String email;
  private String userName;
  private String passwordHash;
  private Boolean isActive;
}
