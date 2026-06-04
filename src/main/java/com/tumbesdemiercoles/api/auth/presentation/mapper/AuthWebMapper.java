package com.tumbesdemiercoles.api.auth.presentation.mapper;

import com.tumbesdemiercoles.api.auth.application.dto.AuthResponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthTokenReponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.LoginRequestDto;
import com.tumbesdemiercoles.api.auth.application.dto.RefreshTokenRequestDto;
import com.tumbesdemiercoles.api.auth.application.dto.RegisterRequestDto;
import com.tumbesdemiercoles.api.auth.application.dto.UserAuthDto;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.LoginRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.RefreshTokenRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.RegisterRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthCreateTokenResponse;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthTokenResponse;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.UserAuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthWebMapper {

  LoginRequestDto toDto(LoginRequest request);
  // Convierte el JSON de entrada al DTO que el Caso de Uso entiende
  RegisterRequestDto toDto(RegisterRequest request);

  RefreshTokenRequestDto toDto(RefreshTokenRequest request);

  // Cambia el tipo de retorno por tu clase
  AuthCreateTokenResponse toWebResponse(AuthResponseDto responseDto);

  AuthTokenResponse toTokenWebResponse(AuthTokenReponseDto responseDto);

  UserAuthResponse toUserWebResponse(UserAuthDto userDto);
}
