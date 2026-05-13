package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/permissions")
public interface PermissionControllerApi {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<PermissionResponse>>> findAllPermissions();

  @GetMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PermissionResponse>> getPermissionById(@PathVariable UUID permissionId);

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<PermissionResponse>> createPermission(@Valid @RequestBody PermissionCreateRequest permissionCreateRequest);

  @PatchMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PermissionResponse>> updatePermission(@PathVariable UUID permissionId, @Valid @RequestBody PermissionUpdateRequest permissionUpdateRequest);

  @DeleteMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deletePermission(@PathVariable UUID permissionId);

}
