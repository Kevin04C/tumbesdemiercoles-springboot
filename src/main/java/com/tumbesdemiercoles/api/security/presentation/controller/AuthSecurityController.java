package com.tumbesdemiercoles.api.security.presentation.controller;

import com.tumbesdemiercoles.api.security.presentation.api.AuthSecurityControllerApi;
import com.tumbesdemiercoles.api.security.services.AccountSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthSecurityController implements AuthSecurityControllerApi {

  private final AccountSecurityService accountSecurityService;

  @Override
  public Mono<String> verifyEmail(String token) {
    return accountSecurityService.verifyEmail(token);
  }
}
