package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.shared.exception.ConflictException;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Crear un nuevo usuario.
 */
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Mono<UserResponseDto> execute(UserRequestDto dto) {

    return userRepository.existsByEmail(dto.getEmail())
        .filter(exists -> !exists)
        .switchIfEmpty(Mono.error(() -> ConflictException.forDuplicate("User", "email", dto.getEmail())))
        .flatMap(isEmailAvailable -> {

          String encodedPassword = passwordEncoder.encode(dto.getPassword());
          User user = User.createNewUser(
              dto.getFirstName(),
              dto.getLastName(),
              dto.getEmail(),
              encodedPassword,
              dto.getImageUrl()
          );
          return userRepository.save(user);
        })
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