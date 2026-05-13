package com.tumbesdemiercoles.api.user.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.BasePaginated;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserFilter  extends BasePaginated {

  private UUID userId;
  private String firstName;
  private String lastName;
  private String email;

}
