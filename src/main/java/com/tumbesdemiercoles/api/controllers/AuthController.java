package com.tumbesdemiercoles.api.controllers;

import com.tumbesdemiercoles.api.controllers.dto.LoginRequest;
import com.tumbesdemiercoles.api.controllers.dto.LoginResponse;
import com.tumbesdemiercoles.api.controllers.interfaces.AutenticacionApi;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Tag(name = "Login", description = "Inicio de Sesion de los Usuarios")
@RequiredArgsConstructor
@RestController
public class AuthController implements AutenticacionApi {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

  @Override
  public Mono<ResponseEntity<LoginResponse>> login(Mono<LoginRequest> loginRequest, ServerWebExchange exchange) {

      return loginRequest.flatMap(request ->
              userDetailsService.findByUsername(request.getEmail())
                      .publishOn(Schedulers.boundedElastic())
                      .filter(userDetails -> passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))
                      .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas")))
                      .map(userDetails -> {
                          UserPrincipal user = (UserPrincipal) userDetails;
                          String token = jwtUtil.generateToken(user);

                          LoginResponse response = new LoginResponse();
                          response.setToken(token);

                          return ResponseEntity.ok(response);
                      })
              );
  }
}