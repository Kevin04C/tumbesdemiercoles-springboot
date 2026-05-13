package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.RoleRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.UpdateRoleUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.RoleNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.Role;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateRoleUseCaseImpl implements UpdateRoleUseCase {

  private final RoleRepository roleRepository;

  @Override
  public Mono<RoleResponseDto> updateRole(UUID roleId, RoleRequestDto roleRequestDto) {
    return roleRepository.findById(roleId)
        .switchIfEmpty(Mono.error(new RoleNotFoundException(roleId)))
        .map(existingRole -> {
          String newName = roleRequestDto.getName() != null ? roleRequestDto.getName() : existingRole.getName();
          existingRole.updateName(newName);
          return existingRole;
        })
        .flatMap(roleRepository::save)
        .map(this::toResponse);
  }

  private RoleResponseDto toResponse(Role role) {
    return RoleResponseDto.builder()
        .id(role.getId())
        .name(role.getName())
        .statusRegistry(role.getStatusRegistry())
        .build();
  }

}
