package com.tumbesdemiercoles.api.auth.presentation.api;

import com.tumbesdemiercoles.api.auth.presentation.dto.request.LoginRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.request.RegisterRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthCreateTokenResponse;
import com.tumbesdemiercoles.api.auth.presentation.dto.response.AuthTokenResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Tag(name = "Auth", description = "Endpoints para el registro, inicio de sesión y gestión de credenciales")
@RequestMapping("/api/v1/auth")
public interface AuthControllerApi {

  @Operation(summary = "Iniciar sesión", description = "Autentica al usuario en el sistema con correo electrónico y contraseña. Retorna el token JWT.")
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<AuthTokenResponse>> login(@Valid @RequestBody LoginRequest request);

  @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en la plataforma con el rol predeterminado y retorna el token JWT inicial.")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<AuthCreateTokenResponse>> register(@Valid @RequestBody RegisterRequest request);


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
