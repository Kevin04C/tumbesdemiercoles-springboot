package com.tumbesdemiercoles.api.access.presentation.mapper;

import com.tumbesdemiercoles.api.access.application.dto.RoleRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleWebMapper {

  RoleRequestDto toDto(RoleCreateRequest request);

  RoleRequestDto toUpdateDto(RoleUpdateRequest request);

  RoleResponse toResponse(RoleResponseDto dto);

}
