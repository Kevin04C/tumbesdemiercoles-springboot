package com.tumbesdemiercoles.api.auth.infrastructure.adapter.external;

import com.tumbesdemiercoles.api.access.application.usecase.AssignUserRoleUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserRolAccessPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRoleAccessAdapter implements UserRolAccessPort {

  private final AssignUserRoleUseCase assignUserRoleUseCase;

  @Override
  public Mono<Void> assignRole(UUID userId, String roleName) {
    return assignUserRoleUseCase.execute(userId, roleName);
  }
}
