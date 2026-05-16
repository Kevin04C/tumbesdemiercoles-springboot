package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RoleResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/roles")
public interface RoleControllerApi {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<RoleResponse>>> findAllRoles();

  @GetMapping("/{roleId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<RoleResponse>> getRoleById(@PathVariable UUID roleId);

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleCreateRequest roleCreateRequest);

  @PatchMapping("/{roleId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<RoleResponse>> updateRole(@PathVariable UUID roleId, @Valid @RequestBody RoleUpdateRequest roleUpdateRequest);

  @DeleteMapping("/{roleId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deleteRole(@PathVariable UUID roleId);

}
