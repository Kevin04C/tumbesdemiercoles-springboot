package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import com.tumbesdemiercoles.api.access.application.ports.out.UserExistencePort;
import com.tumbesdemiercoles.api.access.application.ports.out.UserPermissionEventPublisherPort;
import com.tumbesdemiercoles.api.shared.constants.StatusRegistryConst;
import com.tumbesdemiercoles.api.shared.exception.ConflictException;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AssignUserRoleUseCase {

  private final UserRoleRepository userRoleRepository;
  private final RoleRepository roleRepository;
  private final UserExistencePort userExistencePort;
  private final UserPermissionEventPublisherPort userPermissionEventPublisherPort;

  @Transactional
  public Mono<UserRole> execute(UUID userId, UUID roleId) {
    // Ejecutamos ambas validaciones de forma concurrente
    return Mono.when(validateUserExists(userId), validateRoleExists(roleId))
        // Si ambas terminan exitosamente, procedemos a asignar el rol
        .then(Mono.defer(() -> assignOrReactivateRole(userId, roleId)))
        .delayUntil(userRole -> userPermissionEventPublisherPort.publishPermissionsChanged(userId));
  }


  private Mono<Void> validateUserExists(UUID userId) {
    return userExistencePort.existsById(userId)
        .filter(Boolean::booleanValue)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("User does not exist")))
        .then();
  }

  private Mono<Void> validateRoleExists(UUID roleId) {
    return roleRepository.findById(roleId)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Role does not exist")))
        .then(); // Retornamos Mono<Void> ya que solo validamos existencia
  }

  private Mono<UserRole> assignOrReactivateRole(UUID userId, UUID roleId) {
    return userRoleRepository.findByUserIdAndRoleId(userId, roleId)
        .flatMap(this::handleExistingRole)
        .switchIfEmpty(Mono.defer(() -> createNewUserRole(userId, roleId)));
  }

  private Mono<UserRole> handleExistingRole(UserRole existingUserRole) {
    if (StatusRegistryConst.ACTIVE.equals(existingUserRole.getStatusRegistry())) {
      return Mono.error(new ConflictException("User already has this role assigned"));
    }

    // Reactivamos el rol si estaba eliminado
    existingUserRole.setStatusRegistry(StatusRegistryConst.ACTIVE);
    return userRoleRepository.save(existingUserRole);
  }

  private Mono<UserRole> createNewUserRole(UUID userId, UUID roleId) {
    UserRole newUserRole = UserRole.assignRole(userId, roleId);
    return userRoleRepository.save(newUserRole);
  }
}
