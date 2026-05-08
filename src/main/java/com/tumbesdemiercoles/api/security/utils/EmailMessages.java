package com.tumbesdemiercoles.api.security.utils;

/**
 * Contiene los mensajes y plantillas relacionados con el envío de correos electrónicos
 * para verificación de cuenta y restablecimiento de contraseña.
 */
public class EmailMessages {
  public static final String VERIFICATION_SUBJECT = "Verifica tu correo";
  public static final String CHANGE_SUBJECT_PASSWORD = "Cambia tu contraseña";
  public static final String CODE_TEXT = "Código: ";
  public static final String BODY_TEXT_VERIFICATION = """
          Gracias por registrarte en nuestra plataforma.
          Para activar tu cuenta, por favor verifica tu correo haciendo clic en el siguiente enlace:
          
          %s
          
          Si no creaste esta cuenta, puedes ignorar este mensaje.
          """;

  public static final String BODY_TEXT_PASSWORD = """
            Recibimos una solicitud para restablecer tu contraseña.
            Si fuiste tú, abre este enlace (válido por tiempo limitado):
          
           %s
          
          Si no fuiste tú, ignora este correo.
          """;

  public static String buildLink(String link, String token) {
    return link + "?token=" + token;
  }
}
