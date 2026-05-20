package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.UpdateUserPermissionExceptionsRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.UpdateUserPermissionExceptionsRequestDto.PermissionOverrideDto;
import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.ManageUserPermissionExceptionsUseCase;
import com.tumbesdemiercoles.api.access.application.ports.out.UserExistencePort;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import com.tumbesdemiercoles.api.access.domain.model.UserPermission;
import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.UserPermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ManageUserPermissionExceptionsUseCaseImpl implements ManageUserPermissionExceptionsUseCase {

  private final UserExistencePort userExistencePort;
  private final UserRoleRepository userRoleRepository;
  private final RolePermissionRepository rolePermissionRepository;
  private final PermissionRepository permissionRepository;
  private final UserPermissionRepository userPermissionRepository;

  @Override
  @Transactional
  public Flux<UserPermissionResponseDto> execute(UUID userId, UpdateUserPermissionExceptionsRequestDto requestDto) {
    return validateUserExists(userId)
        .then(Mono.zip(getDefaultPermissionIds(userId), getExistingOverrides(userId)))
        .flatMapMany(tuple -> processOverrides(userId, requestDto.getOverrides(), tuple.getT1(), tuple.getT2()))
        .thenMany(Flux.defer(() -> userPermissionRepository.findByUserId(userId)))
        .map(this::toResponse);
  }

  private Mono<Boolean> validateUserExists(UUID userId) {
    return userExistencePort.existsById(userId)
        .filter(Boolean::booleanValue)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("User", userId)));
  }

  private Mono<Set<UUID>> getDefaultPermissionIds(UUID userId) {
    return userRoleRepository.findByUserId(userId)
        .filter(ur -> "ACTIVE".equals(ur.getStatusRegistry()))
        .map(UserRole::getRoleId)
        .flatMap(rolePermissionRepository::findByRoleId)
        .filter(rp -> "ACTIVE".equals(rp.getStatusRegistry()))
        .map(RolePermission::getPermissionId)
        .collect(Collectors.toSet());
  }

  private Mono<Map<UUID, UserPermission>> getExistingOverrides(UUID userId) {
    return userPermissionRepository.findByUserId(userId)
        .collectMap(UserPermission::getPermissionId);
  }

  private Flux<Void> processOverrides(UUID userId, List<PermissionOverrideDto> overrides, Set<UUID> defaultPermissionIds, Map<UUID, UserPermission> existingOverrides) {
    return Flux.fromIterable(overrides)
        .flatMap(overrideDto -> processSingleOverride(userId, overrideDto, defaultPermissionIds, existingOverrides));
  }

  private Mono<Void> processSingleOverride(UUID userId, PermissionOverrideDto overrideDto, Set<UUID> defaultPermissionIds, Map<UUID, UserPermission> existingOverrides) {
    UUID permId = overrideDto.getPermissionId();
    Boolean desiredState = overrideDto.getIsActive();
    boolean defaultState = defaultPermissionIds.contains(permId);

    return permissionRepository.findById(permId)
        .switchIfEmpty(Mono.error(new PermissionNotFoundException(permId)))
        .flatMap(permission -> applyOverrideLogic(userId, permId, desiredState, defaultState, existingOverrides.get(permId)));
  }

  private Mono<Void> applyOverrideLogic(UUID userId, UUID permId, boolean desiredState, boolean defaultState, UserPermission existingOverride) {
    return desiredState == defaultState
        ? executeDeleteScenario(userId, permId, existingOverride)
        : executeSaveScenario(userId, permId, desiredState, existingOverride);
  }

  private Mono<Void> executeDeleteScenario(UUID userId, UUID permId, UserPermission existingOverride) {
    return Mono.justOrEmpty(existingOverride)
        .flatMap(override -> userPermissionRepository.deleteByUserIdAndPermissionId(userId, permId));
  }

  private Mono<Void> executeSaveScenario(UUID userId, UUID permId, boolean desiredState, UserPermission existingOverride) {
    return existingOverride != null
        ? updateExistingOverride(existingOverride, desiredState)
        : createNewOverride(userId, permId, desiredState);
  }

  private Mono<Void> updateExistingOverride(UserPermission existing, boolean desiredState) {
    return Mono.just(existing)
        .filter(e -> e.getIsActive() != desiredState || !"ACTIVE".equals(e.getStatusRegistry()))
        .doOnNext(e -> e.updateActive(desiredState))
        .flatMap(userPermissionRepository::save)
        .then();
  }

  private Mono<Void> createNewOverride(UUID userId, UUID permId, boolean desiredState) {
    return Mono.fromCallable(() -> UserPermission.builder()
            .userId(userId)
            .permissionId(permId)
            .isActive(desiredState)
            .statusRegistry("ACTIVE")
            .build())
        .flatMap(userPermissionRepository::save)
        .then();
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
