package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.UpdateUserPermissionExceptionsRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
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

  @PutMapping("/exceptions")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<UserPermissionResponse>>> updateExceptions(
      @PathVariable UUID userId,
      @Valid @RequestBody UpdateUserPermissionExceptionsRequest request);

  @GetMapping("/effective")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<PermissionResponse>>> getEffectivePermissions(@PathVariable UUID userId);

}
