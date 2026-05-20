package com.tumbesdemiercoles.api.access.infrastructure.adapter.external;

import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetEffectivePermissionsUseCase;
import com.tumbesdemiercoles.api.security.application.port.out.SecurityPermissionPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class SecurityPermissionAdapter implements SecurityPermissionPort {

  private final GetEffectivePermissionsUseCase getEffectivePermissionsUseCase;

  @Override
  public Flux<String> getPermissionsForUser(UUID userId) {
    return getEffectivePermissionsUseCase.execute(userId)
        .map(PermissionResponseDto::getName);
  }
}
