package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevokeUserRoleUseCase {

  private final UserRoleRepository userRoleRepository;

  @Transactional
  public Mono<UserRole> execute(UUID userId, UUID roleId) {
    return userRoleRepository.findByUserIdAndRoleId(userId, roleId)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("UserRole association not found")))
        .flatMap(userRole -> {
          userRole.revokeRole();
          return userRoleRepository.save(userRole);
        });
  }
}
