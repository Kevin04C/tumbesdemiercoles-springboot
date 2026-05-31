package com.tumbesdemiercoles.api.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String imageUrl;
  private String userName;

}
