package com.tumbesdemiercoles.api.security.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping("/auth")
@Tag(name = "Authentication & Security", description = "Endpoints de seguridad y flujos de autenticación externos, como verificación de email")
public interface AuthSecurityControllerApi {

  @Operation(summary = "Verificar correo electrónico del usuario", description = "Procesa el token enviado por correo electrónico para activar la cuenta de un usuario recién registrado.")
  @GetMapping("/verify-email")
  Mono<String> verifyEmail(
      @Parameter(description = "Token de verificación de email recibido por correo", required = true) @RequestParam String token);
}

