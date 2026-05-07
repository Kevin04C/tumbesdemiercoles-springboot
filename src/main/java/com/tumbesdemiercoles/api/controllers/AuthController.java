package com.tumbesdemiercoles.api.controllers;

import com.tumbesdemiercoles.api.controllers.dto.LoginRequest;
import com.tumbesdemiercoles.api.controllers.dto.LoginResponse;
import com.tumbesdemiercoles.api.controllers.interfaces.AutenticacionApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Tag(name = "Login", description = "Inicio de Sesion de los Usuarios")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AutenticacionApi {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GoogleAuthService googleAuthService;


  @Operation(summary = "Iniciar Sesion")
  @PostMapping("/login")
  public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
    return userDetailsService.findByUsername(request.getEmail())
        .filter(userDetails -> passwordEncoder.matches(request.getPassword(),userDetails.getPassword()))
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas")))
        .map(userDetails -> {
          UserPrincipal user = (UserPrincipal) userDetails;
          String token = jwtUtil.generateToken(user);
          return ResponseEntity.ok(new LoginResponse(token));
        });
  }

  @Override
  public Mono<ResponseEntity<LoginResponse>> login(Mono<LoginRequest> loginRequest, ServerWebExchange exchange) {
    return null;
  }
}