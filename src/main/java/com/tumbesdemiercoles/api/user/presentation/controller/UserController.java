package com.tumbesdemiercoles.api.user.presentation.controller;

import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import com.tumbesdemiercoles.api.user.application.usecase.CreateUserUseCase;
import com.tumbesdemiercoles.api.user.application.usecase.GetUserUseCase;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.presentation.api.UserControllerApi;
import com.tumbesdemiercoles.api.user.presentation.dto.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.UserResponse;
import com.tumbesdemiercoles.api.user.presentation.mapper.UserWebMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

  private final CreateUserUseCase createUserUseCase;
  private final GetUserUseCase getUserUseCase;
  private final UserWebMapper webMapper;

  @Override
  public Mono<ApiResponse<UserResponse>> createUser(UserRequest userRequest) {
    return Mono.just(userRequest)
        .map(webMapper::toDto)
        .flatMap(createUserUseCase::execute)
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Usuario creado exitosamente"));
  }

  @Override
  public Flux<UserResponse> getAllUsers() {
    return getUserUseCase.getAll()
        .map(webMapper::toResponse);
  }

//  @Override
//  public Mono<UserResponse> getUserById(@PathVariable UUID id) {
//    return getUserUseCase.getById(id)
//        .map(webMapper::toResponse)
//
//        .switchIfEmpty(Mono.error(() -> new NotFoundException("User", "id", id.toString())));
//  }
}
