package com.tumbesdemiercoles.api.shared.application.dto.permissionFilter;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionFilter extends BasePageRequest {

  private String name;

  @Builder.Default
  private String sortBy = "id";
}
