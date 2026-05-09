package com.tumbesdemiercoles.api.shared.dto.permissionFilter;

import com.tumbesdemiercoles.api.shared.dto.BasePageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePermissionFilter extends BasePageRequestDto {

  private String keyword;

  @Override
  public String getSortBy() {
    return super.getSortBy() != null && super.getSortBy().equals("userId")
        ? "PermissionName"
        : super.getSortBy();
  }
}
