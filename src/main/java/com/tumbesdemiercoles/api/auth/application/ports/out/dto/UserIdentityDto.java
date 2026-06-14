package com.tumbesdemiercoles.api.auth.application.ports.out.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserIdentityDto {
  private String email;
  private String firstName;
  private String lastName;
  private String encodedPassword;
  private String imageUrl;
  private String userName;
  private String role;
}
