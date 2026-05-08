package com.tumbesdemiercoles.api.security.services;

import com.litanocg.digitalcourse.security.configuration.EmailProps;
import com.litanocg.digitalcourse.security.utils.EmailMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Servicio encargado de enviar correos electrónicos, como la verificación de cuentas
 * y la recuperación de contraseñas, utilizando el servicio de correo configurado.
 *
 * <p>Este servicio interactúa con {@link JavaMailSender} para enviar mensajes simples
 * y utiliza {@link Mono} de Project Reactor para operar de manera asíncrona.</p>
 *
 * <p>La configuración de los correos (como la dirección de "de" y las URLs de los enlaces)
 * se obtiene de la configuración {@link EmailProps}
 * y de mensajes estáticos en {@link EmailMessages}.</p>
 */
@Slf4j
@AllArgsConstructor
@Service
public class EmailService {

  private final JavaMailSender mailSender;
  private final EmailProps props;

  private String getFromAddress() {
    JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
    String username = impl.getUsername();

    String from = (props.getFrom() != null && !props.getFrom().isBlank())
            ? props.getFrom()
            : username;
    return from;
  }

  private SimpleMailMessage buildMessage(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(getFromAddress());
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);
    return message;
  }

  private SimpleMailMessage createEmailMessage(
      String to,
      String token,
      String link,
      String bodyMessage,
      String subjectMessage) {
    String body = bodyMessage.formatted(link) + "\n\n" + EmailMessages.CODE_TEXT + token;
    return buildMessage(to, subjectMessage, body);
  }

  private Mono<Void> sendMessageData(SimpleMailMessage message) {
    return Mono.defer(() -> {
      mailSender.send(message);
      return Mono.empty();
    }).subscribeOn(Schedulers.boundedElastic()).then();
  }

  /**
   * Envía un correo de verificación al usuario con el token correspondiente.
   *
   * @param to    la dirección de correo del destinatario.
   * @param token el token de verificación.
   * @return un {@link Mono} vacío que completa cuando el correo ha sido enviado.
   */
  public Mono<Void> sendVerification(String to, String token) {
    SimpleMailMessage message = createEmailMessage(
            to,
            token,
            EmailMessages.buildLink(props.getFrontendVerifyEmailUrl(), token),
            EmailMessages.BODY_TEXT_VERIFICATION,
            EmailMessages.VERIFICATION_SUBJECT);
    return sendMessageData(message);
  }

  /**
   * Envía un correo de restablecimiento de contraseña al usuario con el token correspondiente.
   *
   * @param to    la dirección de correo del destinatario.
   * @param token el token de restablecimiento de contraseña.
   * @return un {@link Mono} vacío que completa cuando el correo ha sido enviado.
   */
  public Mono<Void> sendPasswordReset(String to, String token) {
    SimpleMailMessage message = createEmailMessage(
            to,
            token,
            EmailMessages.buildLink(props.getFrontendChangePasswordUrl(), token),
            EmailMessages.BODY_TEXT_PASSWORD,
            EmailMessages.CHANGE_SUBJECT_PASSWORD);
    return sendMessageData(message);
  }

}


