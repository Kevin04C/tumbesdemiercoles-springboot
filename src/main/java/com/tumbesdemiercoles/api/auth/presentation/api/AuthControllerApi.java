package com.tumbesdemiercoles.api.auth.presentation.api;

import com.tumbesdemiercoles.api.auth.presentation.dto.request.LoginRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.RegisterRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthCreateTokenResponse;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthTokenResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/auth")
public interface AuthControllerApi {

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<AuthTokenResponse>> login(@Valid @RequestBody LoginRequest request);

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ApiResponse<AuthCreateTokenResponse>>register(@Valid @RequestBody RegisterRequest request);

//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  Mono<ApiResponse<void>> refreshToken();
//
//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  Mono<ApiResponse<void>> forgotPassword();
//
//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  Mono<ApiResponse<void>> resetPassword();
//
//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  Mono<ApiResponse<void>> logout();
//
//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  Mono<ApiResponse<void>> changePassword();


}
