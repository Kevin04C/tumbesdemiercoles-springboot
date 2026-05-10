package com.tumbesdemiercoles.api.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.UUID;
import lombok.Data;

@Data
@JsonPropertyOrder({
    "id",
    "firstName",
    "lastName",
    "email",
    "imageUrl",
    "emailVerified"
})
public class UserResponse {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private Boolean emailVerified;
}
