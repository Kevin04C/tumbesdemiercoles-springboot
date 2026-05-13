package com.tumbesdemiercoles.api.auth.application.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
  private String email;
  private String password;
}
