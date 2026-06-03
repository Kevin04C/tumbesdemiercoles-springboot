package com.tumbesdemiercoles.api.auth.application.ports.out.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPairDto {
  private String accessToken;
  private String refreshToken;
  private Long expiresIn;
}
