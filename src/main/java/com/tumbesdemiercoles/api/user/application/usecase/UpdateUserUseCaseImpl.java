package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.application.ports.in.UpdateUserUseCase;
import com.tumbesdemiercoles.api.user.domain.exception.UserNotFoundException;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Actualizar un usuario existente.
 */
@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

  private final UserRepository userRepository;

  public Mono<UserResponseDto> UpdateUser(UUID id, UserRequestDto userRequestDto) {

    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
        .map(existingUser -> {
          String newFirstName = userRequestDto.getFirstName() != null ? userRequestDto.getFirstName() : existingUser.getFirstName();
          String newLastName = userRequestDto.getLastName() != null ? userRequestDto.getLastName() : existingUser.getLastName();
          String newImageUrl = userRequestDto.getImageUrl() != null ? userRequestDto.getImageUrl() : existingUser.getImageUrl();

          existingUser.updateProfile(newFirstName, newLastName, newImageUrl);
          return existingUser;
        })
        .flatMap(userRepository::save)
        .map(this::toResponse);
  }

  private UserResponseDto toResponse(User user) {
    return UserResponseDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .imageUrl(user.getImageUrl())
        .build();
  }

}
