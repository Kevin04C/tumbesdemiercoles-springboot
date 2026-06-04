package com.tumbesdemiercoles.api.security.configuration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de configuración relacionadas con el envío de correos electrónicos
 * y la gestión de enlaces de verificación y restablecimiento de contraseñas.
 *
 * <p>Esta clase se utiliza para cargar la configuración relacionada con el
 * correo electrónico a través de {@link ConfigurationProperties}, y se espera que
 * los valores sean proporcionados en el archivo de propiedades de la aplicación
 * (por ejemplo, {@code application.properties} o {@code application.yml}).</p>
 */
@ConfigurationProperties(prefix = "app.mail")
public class EmailProps {
  @Email
  @NotBlank
  private String from;

  @NotBlank
  @URL
  private String frontendVerifyEmailUrl;

  @NotBlank
  @URL
  private String frontendChangePasswordUrl;

  @NotBlank
  @URL
  private String frontendResetUrl;

  private int resetPasswordTokenTtlMinutes = 15;
  private int tokenUserTtlHours = 24;
  private int tokenUserTtlMinutes = 15;
  private int verificationEmailTokenTtlHours = 2;
  private int refreshTokenTtlDays = 7;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getFrontendVerifyEmailUrl() {
    return frontendVerifyEmailUrl;
  }

  public void setFrontendVerifyEmailUrl(String frontendVerifyEmailUrl) {
    this.frontendVerifyEmailUrl = frontendVerifyEmailUrl;
  }

  public String getFrontendChangePasswordUrl() {
    return frontendChangePasswordUrl;
  }

  public void setFrontendChangePasswordUrl(String frontendChangePasswordUrl) {
    this.frontendChangePasswordUrl = frontendChangePasswordUrl;
  }

  public String getFrontendResetUrl() {
    return frontendResetUrl;
  }

  public void setFrontendResetUrl(String frontendResetUrl) {
    this.frontendResetUrl = frontendResetUrl;
  }

  public int getResetPasswordTokenTtlMinutes() {
    return resetPasswordTokenTtlMinutes;
  }

  public void setResetPasswordTokenTtlMinutes(int resetPasswordTokenTtlMinutes) {
    this.resetPasswordTokenTtlMinutes = resetPasswordTokenTtlMinutes;
  }

  public int getTokenUserTtlHours() {
    return tokenUserTtlHours;
  }

  public void setTokenUserTtlHours(int tokenUserTtlHours) {
    this.tokenUserTtlHours = tokenUserTtlHours;
  }

  public int getVerificationEmailTokenTtlHours() {
    return verificationEmailTokenTtlHours;
  }

  public void setVerificationEmailTokenTtlHours(int verificationEmailTokenTtlhour) {
    this.verificationEmailTokenTtlHours = verificationEmailTokenTtlhour;
  }

  public int getTokenUserTtlMinutes() {
    return tokenUserTtlMinutes;
  }

  public void setTokenUserTtlMinutes(int tokenUserTtlMinutes) {
    this.tokenUserTtlMinutes = tokenUserTtlMinutes;
  }

  public int getRefreshTokenTtlDays() {
    return refreshTokenTtlDays;
  }

  public void setRefreshTokenTtlDays(int refreshTokenTtlDays) {
    this.refreshTokenTtlDays = refreshTokenTtlDays;
  }
}