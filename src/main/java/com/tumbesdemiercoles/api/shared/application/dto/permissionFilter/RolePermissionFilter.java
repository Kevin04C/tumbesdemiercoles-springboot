package com.tumbesdemiercoles.api.shared.application.dto.permissionFilter;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePermissionFilter extends BasePageRequest {

  private String keyword;

  @Override
  public String getSortBy() {
    return super.getSortBy() != null && super.getSortBy().equals("id")
        ? "PermissionName"
        : super.getSortBy();
  }
}
