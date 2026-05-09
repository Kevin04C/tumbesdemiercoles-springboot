package com.tumbesdemiercoles.api.user.presentation.mapper;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.presentation.dto.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserWebMapper {


  UserRequestDto toDto(UserRequest request);

  UserResponse toResponse(UserResponseDto responseDto);

}
