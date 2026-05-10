package com.tumbesdemiercoles.api.user.presentation.api;

import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import com.tumbesdemiercoles.api.user.presentation.dto.UserRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.UserResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/users")
public interface UserControllerApi {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest);

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
   Flux<UserResponse> getAllUsers();

//  @GetMapping("/{id}")
//  @ResponseStatus(HttpStatus.OK)
//   Mono<UserResponse> getUserById(@PathVariable UUID id);
}
