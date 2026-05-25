package com.tumbesdemiercoles.api.access.presentation.mapper;

import com.tumbesdemiercoles.api.access.application.dto.AssignRolePermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignRolePermissionRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RolePermissionResponse;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePermissionWebMapper {

  RolePermissionResponse toResponse(RolePermissionResponseDto dto);

  @Mapping(target = "roleId", source = "roleId")
  @Mapping(target = "permissionIds", source = "request.permissionIds")
  AssignRolePermissionRequestDto toDto(UUID roleId, AssignRolePermissionRequest request);

}
