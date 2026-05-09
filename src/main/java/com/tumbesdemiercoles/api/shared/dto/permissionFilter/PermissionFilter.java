package com.tumbesdemiercoles.api.shared.dto.permissionFilter;

import com.tumbesdemiercoles.api.shared.dto.BasePageRequestDto;
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
public class PermissionFilter extends BasePageRequestDto {

  private String name;

  @Builder.Default
  private String sortBy = "permissionId";
}
