package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.AssignRolePermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.AssignRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.out.UserPermissionEventPublisherPort;
import com.tumbesdemiercoles.api.access.domain.exception.RoleNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AssignRolePermissionUseCaseImpl implements AssignRolePermissionUseCase {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final RolePermissionRepository rolePermissionRepository;
  private final UserRoleRepository userRoleRepository;
  private final UserPermissionEventPublisherPort userPermissionEventPublisherPort;

  @Override
  @Transactional
  public Flux<RolePermissionResponseDto> execute(AssignRolePermissionRequestDto requestDto) {
    UUID roleId = requestDto.getRoleId();
    Set<UUID> requestedPermissionIds = Set.copyOf(requestDto.getPermissionIds());

    return roleRepository.findById(roleId)
        .switchIfEmpty(Mono.error(() -> new RoleNotFoundException(roleId)))
        .then(
            Mono.zip(
                permissionRepository.findAllById(List.copyOf(requestedPermissionIds))
                    .map(permission -> permission.getId())
                    .collectList()
                    .map(Set::copyOf),

                rolePermissionRepository.findByRoleId(roleId)
                    .map(RolePermission::getPermissionId)
                    .collectList()
                    .map(Set::copyOf)
            )
        )
        .flatMapMany(tuple -> {
          Set<UUID> validPermissionsInDb = tuple.getT1();
          Set<UUID> existingPermissionsForRole = tuple.getT2();

          Optional<UUID> invalidPermissionId = requestedPermissionIds.stream()
              .filter(reqId -> !validPermissionsInDb.contains(reqId))
              .findFirst();

          return invalidPermissionId.isPresent()
              ? Flux.error(() -> new PermissionNotFoundException(invalidPermissionId.get()))
              : processAndSavePermissions(roleId, requestedPermissionIds, existingPermissionsForRole);
        })
        .collectList()
        .flatMapMany(saved -> invalidateUsersCacheForRole(roleId).thenMany(Flux.fromIterable(saved)));
  }

  private Mono<Void> invalidateUsersCacheForRole(UUID roleId) {
    return userRoleRepository.findActiveByRoleId(roleId)
        .flatMap(ur -> userPermissionEventPublisherPort.publishPermissionsChanged(ur.getUserId()))
        .then();
  }


  private Flux<RolePermissionResponseDto> processAndSavePermissions(
      UUID roleId, Set<UUID> requestedIds, Set<UUID> existingIds) {

    List<RolePermission> newPermissionsToSave = requestedIds.stream()
        .filter(reqId -> !existingIds.contains(reqId))
        .map(reqId -> RolePermission.assignPermission(roleId, reqId))
        .toList();

    return newPermissionsToSave.isEmpty()
        ? Flux.empty()
        : rolePermissionRepository.saveAll(newPermissionsToSave).map(this::toResponse);
  }

  private RolePermissionResponseDto toResponse(RolePermission rolePermission) {
    return RolePermissionResponseDto.builder()
        .id(rolePermission.getId())
        .roleId(rolePermission.getRoleId())
        .permissionId(rolePermission.getPermissionId())
        .statusRegistry(rolePermission.getStatusRegistry())
        .build();
  }
}
