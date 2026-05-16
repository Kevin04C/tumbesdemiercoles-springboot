package com.tumbesdemiercoles.api.access.presentation.mapper;

import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RolePermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionWebMapper {

  RolePermissionResponse toResponse(RolePermissionResponseDto dto);

}
