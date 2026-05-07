package com.tumbesdemiercoles.api.security.configuration;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración que habilita la carga de
 * propiedades relacionadas con el correo electrónico.
 *
 * <p>Esta clase se encarga de habilitar el uso de las propiedades definidas en {@link EmailProps}
 * para ser inyectadas en el contexto de la aplicación,
 * permitiendo su acceso a lo largo del sistema.
 *
 * @see EmailProps para la definición de las propiedades
 *     específicas de configuración del correo electrónico
 */
@Configuration
@EnableConfigurationProperties(EmailProps.class)
public class EmailConfig {
}
