package com.tumbesdemiercoles.api.user.infrastructure.event_listener;

import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import com.tumbesdemiercoles.api.shared.application.ports.out.EmailPort;
import com.tumbesdemiercoles.api.user.domain.event.UserRegisteredEvent;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendVerificationEmailOnUserRegistered {

  private final EmailPort emailPort;
  private final JwtUtil jwtUtil;

  @EventListener
  public void onUserRegistered(UserRegisteredEvent event) {
    UUID userId = event.user().getId();
    String email = event.user().getEmail();

    String token = jwtUtil.generateEmailToken(userId);

    emailPort.sendVerification(email, token)
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
        .subscribe(
            success -> log.info("Correo de verificación enviado a {}", email),
            error -> log.error("Fallo definitivo al enviar correo a {} después de 3 reintentos", email, error)
        );
  }
}
