package com.tumbesdemiercoles.api.facade;

import com.tumbesdemiercoles.api.controllers.dto.UserRequest;
import com.tumbesdemiercoles.api.controllers.dto.UserResponse;
import com.tumbesdemiercoles.api.entities.mappers.UserMapper;
import com.tumbesdemiercoles.api.services.definition.UserService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserFacade {

  private final UserService userService;
  private final UserMapper userMapper;

  public Mono<ResponseEntity<UserResponse>> createUser(Mono<UserRequest> requestMono) {
    return requestMono
        .map(userMapper::toEntity)
        .flatMap(userService::createUser)
        .map(userMapper::toResponse)
        .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
  }

  // ==========================================
  // GET: Listar todos (Manejo de Flux)
  // ==========================================
  public Mono<ResponseEntity<Flux<UserResponse>>> getAllUsers() {
    // 1. Pedimos el flujo de entidades al Service
    Flux<UserResponse> fluxResponses = userService.getAllUsers()
        // 2. A medida que van saliendo de la BD, los traducimos a DTO
        .map(userMapper::toResponse);

    // 3. Envolvemos todo el flujo en un HTTP 200 OK
    return Mono.just(ResponseEntity.ok(fluxResponses));
  }

  // ==========================================
  // GET: Buscar por ID (Manejo de Mono y 404)
  // ==========================================
  public Mono<ResponseEntity<UserResponse>> getUserById(UUID id) {
    return userService.getUserById(id)
        .map(userMapper::toResponse)
        .map(response -> ResponseEntity.ok(response))
        // ¡LA MAGIA AQUÍ!: Si el Service no encuentra el usuario en la BD,
        // el Mono viene vacío. Esta línea lo atrapa y devuelve un HTTP 404 automático.
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }


}