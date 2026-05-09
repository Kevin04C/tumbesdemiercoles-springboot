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
public class RoleFilterDto extends BasePageRequestDto {

  private String searchName;

  @Override
  public String getSortBy() {
    return super.getSortBy() != null && super.getSortBy().equals("id")
        ? "RoleID"
        : super.getSortBy();
  }
}
