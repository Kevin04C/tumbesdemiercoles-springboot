package com.tumbesdemiercoles.api.user.presentation.api;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserFilterRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserUpdateRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.response.UserResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/users")
public interface UserControllerApi {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest);

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PageResponseDto<UserResponse>>> findUsers( @Valid UserFilterRequest userFilterRequest);

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id);

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<UserResponse>> updateUser(
      @PathVariable UUID id,
      @Valid @RequestBody UserUpdateRequest userUpdateRequest
  );
  @DeleteMapping("/{id}")
  Mono<ApiResponse<Void>> deleteUser(@PathVariable UUID id);
}
