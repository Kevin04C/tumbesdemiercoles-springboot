package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;

public interface GetUserPermissionUseCase {
  Flux<UserPermissionResponseDto> getByUserId(UUID userId);
}
