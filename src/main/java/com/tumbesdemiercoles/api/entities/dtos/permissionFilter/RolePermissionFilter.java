package com.tumbesdemiercoles.api.entities.dtos.permissionFilter;

import com.tumbesdemiercoles.api.entities.dtos.BasePageRequestDto;
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
