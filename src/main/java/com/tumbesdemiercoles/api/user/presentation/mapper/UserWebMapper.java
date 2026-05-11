package com.tumbesdemiercoles.api.user.presentation.mapper;

import com.tumbesdemiercoles.api.shared.presentation.mapper.PageMapper;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserUpdateRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserWebMapper extends PageMapper<User,UserResponse> {

  UserRequestDto toDto(UserRequest request);

  UserResponse toResponse(UserResponseDto responseDto);

  UserRequestDto toUpdateDto(UserUpdateRequest request);
}
