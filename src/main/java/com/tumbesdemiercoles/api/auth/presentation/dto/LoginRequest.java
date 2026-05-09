package com.tumbesdemiercoles.api.auth.presentation.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
