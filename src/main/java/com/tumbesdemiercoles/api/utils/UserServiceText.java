package com.tumbesdemiercoles.api.utils;

/**
 * Textos y mensajes reutilizados en el servicio de usuarios.
 */
public class UserServiceText {

  private UserServiceText() {

  }

  public static  final String userNotFound = "User not found with id ";
  public static  final String userExistEmailDocId =
          "User already exists with email or document number";
  public static  final String ErrorSendEmail = "error al enviar el email de verificacion ";
  public static  final String roleNameStudent = "STUDENT";
  public static  final String roleNameAdmin = "ADMIN";
  public static  final String internalServerError = "An internal error occurred";
  public static  final String userNotFoundForId = "User not found for id: ";
  public static  final String userNotUpdateWithId = "User not Updated with id: ";
  public static  final String userNotDeleteWithId = "User not deleted with id: ";
  public static  final String statusRegistryInactive = "INACTIVE";
  public static  final String pathCloudinaryUsers = "curso-digital/users";
  public static  final String secureUrl = "secure_url";
  public static  final String emailVerifySuccess = "Email verificado correctamtente ✅";
  public static  final String tokenInvalidOrExpired = "Token inválido o expirado ❌";
  public static  final String userNotFoundForEmail = "User not found for email: ";
  public static  final String tokenStatusValid = "valid";
  public static  final String errorValidToken = "Error al validar el token";
  public static  final String tokenInvalidOrUsed = "Token inválido o ya utilizado";
  public static  final String userNotFoundForDocument ="No se encontró un usuario con el número de documento: ";
  public static  final String userNotBlankDocument = "El número de documento no puede estar vacío";


}
