package com.tumbesdemiercoles.api.auth.presentation.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
