package com.tumbesdemiercoles.api.user.presentation.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UserResponse {
  private UUID userId;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private Boolean emailVerified;
}
