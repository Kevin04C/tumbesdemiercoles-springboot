package com.tumbesdemiercoles.api.user.presentation.mapper;

import com.tumbesdemiercoles.api.shared.presentation.mapper.PageMapper;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.model.UserFilter;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserFilterRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserUpdateRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserWebMapper extends PageMapper<UserResponseDto,UserResponse> {

  UserRequestDto toDto(UserRequest request);

  UserRequestDto toUpdateDto(UserUpdateRequest request);

  @Mapping(target = "page", expression = "java(request.getOffsetPage())")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "sortBy", source = "sortBy")
  @Mapping(target = "sortDirection", source = "sortDir")
  UserFilter toFilter(UserFilterRequest request);
}
