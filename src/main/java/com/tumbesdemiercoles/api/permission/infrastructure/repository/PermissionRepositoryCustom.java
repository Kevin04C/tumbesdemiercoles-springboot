package com.tumbesdemiercoles.api.permission.infrastructure.repository;

import com.tumbesdemiercoles.api.permission.infrastructure.entity.PermissionEntity;
import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.dto.permissionFilter.PermissionFilter;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface PermissionRepositoryCustom {
  Mono<PageResponseDto<PermissionEntity>> findAllPermissionsPaginated(Pageable pageable, PermissionFilter filter);

  Mono<PageResponseDto<PermissionEntity>> findPermissionsByRoleIdPaginated(Long roleId, Pageable pageable, String keyword);
}
