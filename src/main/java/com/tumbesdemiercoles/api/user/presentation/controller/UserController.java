package com.tumbesdemiercoles.api.user.presentation.controller;

import com.tumbesdemiercoles.api.user.application.usecase.CreateUserUseCase;
import com.tumbesdemiercoles.api.user.application.usecase.GetUserUseCase;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.presentation.dto.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.UserResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final CreateUserUseCase createUserUseCase;
  private final GetUserUseCase getUserUseCase;

  @PostMapping
  public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody Mono<UserRequest> userRequest,
      ServerWebExchange exchange) {
    return userRequest
        .map(req -> UserRequestDto.builder()
            .firstName(req.getFirstName())
            .lastName(req.getLastName())
            .email(req.getEmail())
            .password(req.getPassword())
            .imageUrl(req.getImageUrl())
            .build())
        .flatMap(dto -> createUserUseCase.execute(dto))
        .map(result -> {
          UserResponse response = new UserResponse();
          response.setUserId(result.getUserId());
          response.setFirstName(result.getFirstName());
          response.setLastName(result.getLastName());
          response.setEmail(result.getEmail());
          response.setImageUrl(result.getImageUrl());
          response.setEmailVerified(result.getEmailVerified());
          return ResponseEntity.status(HttpStatus.CREATED).body(response);
        });
  }

  @GetMapping
  public Mono<ResponseEntity<Flux<UserResponse>>> getAllUsers(ServerWebExchange exchange) {
    Flux<UserResponse> fluxResponses = getUserUseCase.getAll()
        .map(result -> {
          UserResponse response = new UserResponse();
          response.setUserId(result.getUserId());
          response.setFirstName(result.getFirstName());
          response.setLastName(result.getLastName());
          response.setEmail(result.getEmail());
          response.setImageUrl(result.getImageUrl());
          response.setEmailVerified(result.getEmailVerified());
          return response;
        });

    return Mono.just(ResponseEntity.ok(fluxResponses));
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<UserResponse>> getUserById(@PathVariable UUID id, ServerWebExchange exchange) {
    return getUserUseCase.getById(id)
        .map(result -> {
          UserResponse response = new UserResponse();
          response.setUserId(result.getUserId());
          response.setFirstName(result.getFirstName());
          response.setLastName(result.getLastName());
          response.setEmail(result.getEmail());
          response.setImageUrl(result.getImageUrl());
          response.setEmailVerified(result.getEmailVerified());
          return ResponseEntity.ok(response);
        })
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
