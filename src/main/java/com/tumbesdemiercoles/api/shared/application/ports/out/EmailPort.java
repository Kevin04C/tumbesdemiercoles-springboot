package com.tumbesdemiercoles.api.shared.application.ports.out;

import reactor.core.publisher.Mono;

/**
 * Puerto de salida para la comunicación por correo electrónico.
 * Define el contrato que debe cumplir cualquier proveedor de mensajería
 * externo para satisfacer las necesidades de notificación del sistema.
 */
public interface EmailPort {
  /**
   * Inicia el proceso de notificación para la validación de una nueva cuenta.
   * Este método debe garantizar que el usuario reciba los medios necesarios
   * para confirmar la propiedad de su dirección de correo.
   *
   * @param to    Dirección del destinatario que debe ser notificado.
   * @param token Identificador de seguridad único para el proceso de verificación.
   * @return Un Mono que se completa una vez el proveedor externo acepta el mensaje.
   */
  Mono<Void> sendVerification(String to, String token);

  /**
   * Notifica al usuario las instrucciones para recuperar el acceso a su cuenta.
   * Se utiliza cuando se ha solicitado explícitamente un cambio de credenciales.
   *
   * @param to    Dirección del usuario que solicitó el restablecimiento.
   * @param token Credencial temporal para autorizar el cambio de contraseña.
   * @return Un Mono que representa el estado de la operación de envío.
   */
  Mono<Void> sendPasswordReset(String to, String token);
}
