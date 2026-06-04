package com.tumbesdemiercoles.api.auth.presentation.controller;

import com.tumbesdemiercoles.api.auth.application.ports.In.LoginUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.In.RefreshTokenUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.In.RegisterUseCase;
import com.tumbesdemiercoles.api.auth.presentation.api.AuthControllerApi;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.LoginRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.RefreshTokenRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.RegisterRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthCreateTokenResponse;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthTokenResponse;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthUserResponse;
import com.tumbesdemiercoles.api.auth.presentation.mapper.AuthWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerApi {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final AuthWebMapper authWebMapper;
    private final RefreshTokenUseCase refreshTokenUseCase;


  @Override
  public Mono<ApiResponse<AuthTokenResponse>> login(LoginRequest request) {
    return loginUseCase.login(authWebMapper.toDto(request))
        .map(authWebMapper::toTokenWebResponse)
        .map(tokenResponse -> ApiResponse.success(tokenResponse, "Login exitoso"));
  }

  @Override
  public Mono<ApiResponse<AuthCreateTokenResponse>> register(RegisterRequest request) {
    return registerUseCase.register(authWebMapper.toDto(request))
        .map(authWebMapper::toWebResponse)
        .map(authUserResponse -> ApiResponse.success(authUserResponse, "Usuario Registrado Correctamente"));
  }
  @Override
  public Mono<ApiResponse<AuthTokenResponse>> refreshSession( RefreshTokenRequest refreshTokenRequest) {

    return refreshTokenUseCase.refreshSession(authWebMapper.toDto(refreshTokenRequest))
        .map(authWebMapper::toTokenWebResponse)
        .map(tokenResponse -> ApiResponse.success(tokenResponse, "Sesión renovada exitosamente"));
  }
}
