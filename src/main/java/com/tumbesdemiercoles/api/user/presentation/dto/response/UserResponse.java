package com.tumbesdemiercoles.api.user.presentation.dto.response;

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
    "isEmailVerified",
    "isActive"
})
public class UserResponse {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private Boolean isEmailVerified;
  private Boolean isActive;
}
