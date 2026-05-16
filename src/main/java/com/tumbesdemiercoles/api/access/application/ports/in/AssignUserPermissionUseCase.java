package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.AssignUserPermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import reactor.core.publisher.Flux;

public interface AssignUserPermissionUseCase {
  Flux<UserPermissionResponseDto> execute(AssignUserPermissionRequestDto assignUserPermissionRequestDto);
}
