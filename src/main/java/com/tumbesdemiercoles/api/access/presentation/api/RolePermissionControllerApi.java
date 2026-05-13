package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignRolePermissionRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RolePermissionResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/roles/{roleId}/permissions")
public interface RolePermissionControllerApi {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<RolePermissionResponse>>> getPermissionsByRoleId(@PathVariable UUID roleId);

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<List<RolePermissionResponse>>> assignPermissions(@PathVariable UUID roleId, @Valid @RequestBody AssignRolePermissionRequest assignRolePermissionRequest);

  @DeleteMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> revokePermission(@PathVariable UUID roleId, @PathVariable UUID permissionId);

}
