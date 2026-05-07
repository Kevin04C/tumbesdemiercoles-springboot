package com.tumbesdemiercoles.api.repository.custom;

import com.tumbesdemiercoles.api.entities.Permission;
import com.tumbesdemiercoles.api.entities.dtos.PageResponseDto;
import com.tumbesdemiercoles.api.entities.dtos.permissionFilter.PermissionFilter;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface PermissionRepositoryCustom {
  Mono<PageResponseDto<Permission>> findAllPermissionsPaginated(Pageable pageable, PermissionFilter filter);

  Mono<PageResponseDto<Permission>> findPermissionsByRoleIdPaginated(Long roleId, Pageable pageable, String keyword);
}
