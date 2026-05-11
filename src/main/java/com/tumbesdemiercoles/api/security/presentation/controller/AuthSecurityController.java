package com.tumbesdemiercoles.api.security.presentation.controller;

import com.tumbesdemiercoles.api.security.presentation.api.AuthSecurityControllerApi;
import com.tumbesdemiercoles.api.security.services.AccountSecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthSecurityController implements AuthSecurityControllerApi {

  private final AccountSecurityService accountSecurityService;

  @Override
  public Mono<String> verifyEmail(String token) {

    log.info("Recibida solicitud de verificación de email con token: {}", token);
    return accountSecurityService.verifyEmail(token);
  }
}
