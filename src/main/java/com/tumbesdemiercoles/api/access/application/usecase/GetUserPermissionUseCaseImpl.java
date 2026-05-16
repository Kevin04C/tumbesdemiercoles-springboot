package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetUserPermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.model.UserPermission;
import com.tumbesdemiercoles.api.access.domain.repository.UserPermissionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetUserPermissionUseCaseImpl implements GetUserPermissionUseCase {

  private final UserPermissionRepository userPermissionRepository;

  @Override
  public Flux<UserPermissionResponseDto> getByUserId(UUID userId) {
    return userPermissionRepository.findByUserId(userId)
        .map(this::toResponse);
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
