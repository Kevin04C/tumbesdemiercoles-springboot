package com.tumbesdemiercoles.api.auth.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("token_type")
  private String tokenType; // Usualmente "Bearer"

  @JsonProperty("expires_in")
  private Long expiresIn;   // Tiempo en segundos (ej. 3600)

  private UserAuthResponse user;
}