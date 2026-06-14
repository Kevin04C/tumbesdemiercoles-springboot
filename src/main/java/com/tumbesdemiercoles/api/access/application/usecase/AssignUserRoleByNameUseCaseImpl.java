package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AssignUserRoleByNameUseCaseImpl {

  private final RoleRepository roleRepository;
  private final AssignUserRoleUseCase assignUserRoleUseCase;

  public Mono<UserRole> execute(UUID userId, String roleName) {
    return roleRepository.findByName(roleName)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Role name '" + roleName + "' does not exist")))
        .flatMap(role -> assignUserRoleUseCase.execute(userId, role.getId()));
  }
}