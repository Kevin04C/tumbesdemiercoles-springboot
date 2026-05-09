package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Crear un nuevo usuario.
 */
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;

  public Mono<UserResponseDto> execute(UserRequestDto dto) {
    User user = User.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .email(dto.getEmail())
        .passwordHash(dto.getPassword())
        .emailVerified(false)
        .statusRegistry("ACTIVE")
        .build();

    return userRepository.save(user)
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
