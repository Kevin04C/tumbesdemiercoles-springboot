package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.RoleRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.CreateRoleUseCase;
import com.tumbesdemiercoles.api.access.domain.model.Role;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import com.tumbesdemiercoles.api.shared.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateRoleUseCaseImpl implements CreateRoleUseCase {

  private final RoleRepository roleRepository;

  @Override
  public Mono<RoleResponseDto> execute(RoleRequestDto roleRequestDto) {
    return roleRepository.existsByName(roleRequestDto.getName())
        .filter(exists -> !exists)
        .switchIfEmpty(Mono.error(() -> ConflictException.forDuplicate("Role", "name", roleRequestDto.getName())))
        .flatMap(available -> {
          Role role = Role.createNewRole(roleRequestDto.getName());
          return roleRepository.save(role);
        })
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
