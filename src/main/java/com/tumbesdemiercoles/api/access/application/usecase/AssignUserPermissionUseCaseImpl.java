package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.AssignUserPermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.AssignUserPermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.out.UserExistencePort;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.UserPermission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.UserPermissionRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AssignUserPermissionUseCaseImpl implements AssignUserPermissionUseCase {

  private final UserExistencePort userExistencePort;
  private final PermissionRepository permissionRepository;
  private final UserPermissionRepository userPermissionRepository;

  @Override
  @Transactional
  public Flux<UserPermissionResponseDto> execute(AssignUserPermissionRequestDto assignUserPermissionRequestDto) {
    return userExistencePort.existsById(assignUserPermissionRequestDto.getUserId())
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("User", assignUserPermissionRequestDto.getUserId())))
        .thenMany(Flux.fromIterable(assignUserPermissionRequestDto.getPermissionIds())
            .flatMap(permissionId -> permissionRepository.findById(permissionId)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException(permissionId)))
                .then(userPermissionRepository.existsByUserIdAndPermissionId(
                    assignUserPermissionRequestDto.getUserId(), permissionId))
                .filter(exists -> !exists)
                .flatMap(available -> {
                  UserPermission userPermission = UserPermission.assignPermission(
                      assignUserPermissionRequestDto.getUserId(), permissionId);
                  return userPermissionRepository.save(userPermission);
                })
                .map(this::toResponse)
            ));
  }

  private UserPermissionResponseDto toResponse(UserPermission userPermission) {
    return UserPermissionResponseDto.builder()
        .id(userPermission.getId())
        .userId(userPermission.getUserId())
        .permissionId(userPermission.getPermissionId())
        .isActive(userPermission.getIsActive())
        .statusRegistry(userPermission.getStatusRegistry())
        .build();
  }

}
