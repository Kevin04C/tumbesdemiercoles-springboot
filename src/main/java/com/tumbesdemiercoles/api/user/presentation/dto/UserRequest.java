package com.tumbesdemiercoles.api.user.presentation.dto;

import lombok.Data;

@Data
public class UserRequest {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String imageUrl;
}
