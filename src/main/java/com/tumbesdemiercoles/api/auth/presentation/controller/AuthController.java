package com.tumbesdemiercoles.api.auth.presentation.controller;

import com.tumbesdemiercoles.api.auth.presentation.dto.LoginRequest;
import com.tumbesdemiercoles.api.auth.presentation.dto.LoginResponse;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
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
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public Mono<ResponseEntity<LoginResponse>> login(@RequestBody Mono<LoginRequest> loginRequest, ServerWebExchange exchange) {

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
