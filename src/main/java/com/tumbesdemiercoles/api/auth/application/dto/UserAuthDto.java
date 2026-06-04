package com.tumbesdemiercoles.api.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {
  private UUID id;
  private String email;
  private String userName;
  private List<String> roles;
}