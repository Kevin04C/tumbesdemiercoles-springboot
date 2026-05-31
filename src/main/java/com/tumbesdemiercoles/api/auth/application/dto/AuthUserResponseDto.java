package com.tumbesdemiercoles.api.auth.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponseDto {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private String userName;
  private Boolean isEmailVerified;
  private Boolean isActive;
}
