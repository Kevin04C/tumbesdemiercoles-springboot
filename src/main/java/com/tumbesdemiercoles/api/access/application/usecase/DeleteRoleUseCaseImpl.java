package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.DeleteRoleUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.RoleNotFoundException;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteRoleUseCaseImpl implements DeleteRoleUseCase {

  private final RoleRepository roleRepository;

  @Override
  public Mono<Void> deleteRole(UUID roleId) {
    return roleRepository.findById(roleId)
        .switchIfEmpty(Mono.error(new RoleNotFoundException(roleId)))
        .flatMap(role -> {
          role.deleteRole();
          return roleRepository.save(role);
        })
        .then();
  }

}
