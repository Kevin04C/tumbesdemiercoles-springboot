package com.tumbesdemiercoles.api.access.presentation.mapper;

import com.tumbesdemiercoles.api.access.application.dto.PermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionWebMapper {

  PermissionRequestDto toDto(PermissionCreateRequest request);

  PermissionRequestDto toUpdateDto(PermissionUpdateRequest request);

  PermissionResponse toResponse(PermissionResponseDto dto);

}
