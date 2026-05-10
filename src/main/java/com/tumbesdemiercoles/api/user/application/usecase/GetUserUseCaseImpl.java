package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.application.ports.in.GetUserUseCase;
import com.tumbesdemiercoles.api.user.domain.exception.UserNotFoundException;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Obtener usuario(s) — por ID o listar todos.
 */
@Service
@RequiredArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {

  private final UserRepository userRepository;

  @Override
  public Mono<UserResponseDto> getById(UUID id) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
        .map(this::toResponse);
  }

  @Override
  public Flux<UserResponseDto> getAll() {
    return userRepository.findAll()
        .map(this::toResponse);
  }

  private UserResponseDto toResponse(User user) {
    return UserResponseDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .imageUrl(user.getImageUrl())
        .emailVerified(user.getEmailVerified())
        .build();
  }

}
