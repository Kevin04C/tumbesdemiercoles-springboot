package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetRoleUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.RoleNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.Role;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetRoleUseCaseImpl implements GetRoleUseCase {

  private final RoleRepository roleRepository;

  @Override
  public Mono<RoleResponseDto> getById(UUID roleId) {
    return roleRepository.findById(roleId)
        .switchIfEmpty(Mono.error(new RoleNotFoundException(roleId)))
        .map(this::toResponse);
  }

  @Override
  public Flux<RoleResponseDto> findAll() {
    return roleRepository.findAll()
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
