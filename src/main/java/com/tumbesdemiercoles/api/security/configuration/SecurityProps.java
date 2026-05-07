package com.tumbesdemiercoles.api.security.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Clase de configuración para las propiedades de seguridad de la aplicación.
 * Esta clase está vinculada a las propiedades de configuración con el prefijo "app.security"
 * y contiene los valores necesarios para la seguridad, como el valor de "resetPepper".
 */
@ConfigurationProperties(prefix = "app.security")
public class SecurityProps {

  @NotBlank
  private String resetPepper;

  public String getResetPepper() {
    return resetPepper;
  }

  public void setResetPepper(String resetPepper) {
    this.resetPepper = resetPepper;
  }
}
