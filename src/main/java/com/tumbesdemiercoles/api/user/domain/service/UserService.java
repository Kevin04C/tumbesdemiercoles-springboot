package com.tumbesdemiercoles.api.user.domain.service;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de operaciones de usuario.
 * Define QUÉ se puede hacer con los usuarios.
 * Cada operación es implementada por un UseCase en la capa de aplicación.
 */
public interface UserService {

  Mono<UserResponseDto> createUser(UserRequestDto dto);

  Mono<UserResponseDto> getUserById(UUID id);

  Flux<UserResponseDto> getAllUsers();

  Mono<UserResponseDto> updateUser(UUID id, UserRequestDto dto);

  Mono<Void> deleteUser(UUID id);

}
