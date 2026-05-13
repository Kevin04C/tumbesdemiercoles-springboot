package com.tumbesdemiercoles.api.auth.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "token", "user" })
public class RegisterResponse {

  private String token;
  private AuthUserResponse user;

}
