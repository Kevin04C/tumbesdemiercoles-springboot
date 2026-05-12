package com.tumbesdemiercoles.api.security.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping("/auth")
@Tag(name = "Authentication & Security")
public interface AuthSecurityControllerApi {

  @Operation(summary = "Verificar el Correo Electrónico")
  @GetMapping("/verify-email")
  Mono<String> verifyEmail(@RequestParam String token);
}
