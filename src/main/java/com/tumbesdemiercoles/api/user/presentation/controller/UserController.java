package com.tumbesdemiercoles.api.user.presentation.controller;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.ports.in.CreateUserUseCase;
import com.tumbesdemiercoles.api.user.application.ports.in.DeleteUserUseCase;
import com.tumbesdemiercoles.api.user.application.ports.in.UpdateUserUseCase;
import com.tumbesdemiercoles.api.user.application.usecase.GetUserUseCaseImpl;
import com.tumbesdemiercoles.api.user.presentation.api.UserControllerApi;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserFilterRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserUpdateRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.response.AuthCreateTokenResponse;
import com.tumbesdemiercoles.api.user.presentation.dto.response.UserResponse;
import com.tumbesdemiercoles.api.user.presentation.mapper.UserWebMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

  private final CreateUserUseCase createUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;
  private final GetUserUseCaseImpl getUserUseCase;
  private final UserWebMapper webMapper;

  @Override
  public Mono<ApiResponse<AuthCreateTokenResponse>> createUser(UserRequest userRequest) {
    
    UserRequestDto dto = webMapper.toDto(userRequest);
    return createUserUseCase.execute(dto)
            .map(authResponse -> ApiResponse.success(
                    authResponse,
                    "Usuario registrado y autenticado correctamente"
            ));
  }

  @Override
  public Mono<ApiResponse<PageResponseDto<UserResponse>>> findUsers(UserFilterRequest userFilterRequest) {
    return getUserUseCase.findUsers(userFilterRequest)
        .map(webMapper::toPageResponse)
        .map(pageDto -> ApiResponse.success(pageDto, "Usuarios encontrados"));
  }

  @Override
  public Mono<ApiResponse<UserResponse>> getUserById( UUID id) {
    return getUserUseCase.getById(id)
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Usuario encontrado exitosamente"));
  }

  @Override
  public Mono<ApiResponse<UserResponse>> updateUser(UUID id, UserUpdateRequest userUpdateRequest) {
    return updateUserUseCase.UpdateUser(id, webMapper.toUpdateDto(userUpdateRequest))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Usuario actualizado correctamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> deleteUser(UUID id) {
    return deleteUserUseCase.deleteUser(id)
        .thenReturn(ApiResponse.success(null, "Usuario eliminado correctamente"));
  }


}
