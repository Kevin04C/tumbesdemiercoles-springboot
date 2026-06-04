package com.tumbesdemiercoles.api.auth.application.ports.out.dto;

import java.util.List;
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
  private List<String> roles;
}
