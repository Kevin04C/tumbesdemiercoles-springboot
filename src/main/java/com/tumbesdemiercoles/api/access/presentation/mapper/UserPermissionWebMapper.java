package com.tumbesdemiercoles.api.access.presentation.mapper;

import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserPermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPermissionWebMapper {

  UserPermissionResponse toResponse(UserPermissionResponseDto dto);

}
