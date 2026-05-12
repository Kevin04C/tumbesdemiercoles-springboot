package com.tumbesdemiercoles.api.shared.infrastructure.adapter;

import com.tumbesdemiercoles.api.security.configuration.EmailProps;
import com.tumbesdemiercoles.api.security.utils.EmailMessages;
import com.tumbesdemiercoles.api.shared.application.ports.out.EmailPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Adaptador de infraestructura para el envío de correos electrónicos vía SMTP.
 * Implementa el puerto de salida {@link EmailPort} de la capa de aplicación.
 */
@Slf4j
@AllArgsConstructor
@Component
public class SmtpEmailAdapter implements EmailPort {

  private final JavaMailSender mailSender;
  private final EmailProps props;
  private final TemplateEngine templateEngine;

  /**
   * {@inheritDoc}
   * <p>
   * Esta implementación específica utiliza el protocolo SMTP y aísla la carga
   * en un pool de hilos elástico para no degradar la reactividad de Netty.
   */
  @Override
  public Mono<Void> sendVerification(String to, String token) {
    return Mono.fromCallable(() -> {
          // 1. Armamos el enlace hacia tu frontend en Next.js
          String link = EmailMessages.buildLink(props.getFrontendVerifyEmailUrl(), token);

          // 2. Pasamos las variables a Thymeleaf
          Context context = new Context();
          context.setVariable("verificationUrl", link);
          // (Opcional) Puedes pasar el token suelto por si quieres mostrarlo como texto en el HTML
          context.setVariable("token", token);

          // 3. Procesamos el archivo "email-verification.html"
          String htmlContent = templateEngine.process("email-verification", context);

          // 4. Construimos el mensaje Mime
          return buildMimeMessage(to, EmailMessages.VERIFICATION_SUBJECT, htmlContent);
        })
        .flatMap(this::sendMessageData);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Al igual que la verificación, esta implementación se apoya en SMTP para el envío
   * físico y delega el bloqueo de I/O de red a un scheduler elástico, protegiendo
   * así el hilo de eventos de WebFlux.
   */
  @Override
  public Mono<Void> sendPasswordReset(String to, String token) {
    return Mono.fromCallable(() -> {
          String link = EmailMessages.buildLink(props.getFrontendChangePasswordUrl(), token);

          Context context = new Context();
          context.setVariable("resetUrl", link);
          context.setVariable("token", token);

          // Suponiendo que creas un "email-reset-password.html"
          String htmlContent = templateEngine.process("email-reset-password", context);

          return buildMimeMessage(to, EmailMessages.CHANGE_SUBJECT_PASSWORD, htmlContent);
        })
        .flatMap(this::sendMessageData);
  }

  // --- MÉTODOS PRIVADOS ---

  /**
   * Ejecuta el envío físico del correo electrónico aislando la operación bloqueante de red.
   * Utiliza {@code Schedulers.boundedElastic()} para evitar bloquear el hilo principal de WebFlux.
   *
   * @param message El objeto de mensaje de correo ya construido.
   * @return Un Mono vacío.
   */
  /**
   * Ejecuta el envío físico del correo electrónico aislando la operación bloqueante de red.
   */
  private Mono<Void> sendMessageData(MimeMessage message) {
    return Mono.fromCallable(() -> {
          // Callable permite lanzar excepciones comprobadas sin try-catch
          log.info("Enviando correo HTML a {}", message.getAllRecipients()[0]);
          mailSender.send(message);

          // Retornar null en un fromCallable genera un Mono.empty() internamente
          return null;
        })
        .subscribeOn(Schedulers.boundedElastic())
        // Manejo del error al estilo puramente reactivo
        .doOnError(error -> log.error("Error al enviar el correo o leer destinatarios", error))
        .then();
  }

  /**
   * Método factoría para construir la estructura de un correo HTML.
   */
  private MimeMessage buildMimeMessage(String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    // El 'true' indica que es multipart y soporta HTML
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setFrom(getFromAddress());
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true); // true = indicamos que el texto procesado es HTML

    return message;
  }

  /**
   * Resuelve la dirección de correo remitente ("From").
   * Prioriza la dirección configurada en las propiedades personalizadas; si no existe,
   * hace fallback al nombre de usuario configurado por defecto en el JavaMailSender.
   *
   * @return Un String con el correo electrónico del remitente.
   */
  private String getFromAddress() {
    JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
    return (props.getFrom() != null && !props.getFrom().isBlank()) ? props.getFrom() : impl.getUsername();
  }
}
