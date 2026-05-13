package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignUserPermissionRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserPermissionResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/users/{userId}/permissions")
public interface UserPermissionControllerApi {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<UserPermissionResponse>>> getPermissionsByUserId(@PathVariable UUID userId);

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<List<UserPermissionResponse>>> assignPermissions(@PathVariable UUID userId, @Valid @RequestBody AssignUserPermissionRequest assignUserPermissionRequest);

  @DeleteMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> revokePermission(@PathVariable UUID userId, @PathVariable UUID permissionId);

}
