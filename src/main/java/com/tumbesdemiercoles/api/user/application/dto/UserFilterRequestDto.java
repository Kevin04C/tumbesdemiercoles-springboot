package com.tumbesdemiercoles.api.user.application.dto;

import com.tumbesdemiercoles.api.shared.application.dto.BasePageRequestDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterRequestDto extends BasePageRequestDto {

  private UUID userId;
  private String firstName;
  private String lastName;
  private String email;

}
