package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.shared.exception.ConflictException;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.application.ports.in.CreateUserUseCase;
import com.tumbesdemiercoles.api.user.domain.event.UserRegisteredEvent;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Crear un nuevo usuario.
 */
@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher eventPublisher;

  @Override
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
        .doOnSuccess(user -> {
          eventPublisher.publishEvent(new UserRegisteredEvent(user));
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
        .emailVerified(user.getIsEmailVerified())
        .build();
  }
}