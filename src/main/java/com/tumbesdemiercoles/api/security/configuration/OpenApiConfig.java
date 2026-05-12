package com.tumbesdemiercoles.api.security.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 * de Digital-Courses.
 *
 * <p>Define la información básica de la API (título y versión) y
 * configura el esquema de seguridad basado en autenticación
 * HTTP Bearer con tokens JWT.</p>
 */
@Configuration
public class OpenApiConfig {
  public static final String title = "Tumbes de Miercoles API";
  public static final String bearer = "bearer";
  public static final String bearerAuth = "bearerAuth";
  public static final String version = "1.0";
  public static final String format = "JWT";

  /**
   * Crea y configura la instancia principal de OpenAPI para
   * la documentación de la API.
   *
   * <p>Incluye la información básica (título y versión) y
   * agrega un requisito de seguridad global basado en un
   * esquema HTTP Bearer con formato JWT.</p>
   *
   * @return instancia configurada de {@link OpenAPI}
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
            .info(new Info().title(title).version(version))
            .addSecurityItem(new SecurityRequirement().addList(bearerAuth))
            .components(new Components()
                    .addSecuritySchemes(bearerAuth,
                            new SecurityScheme()
                                    .name(bearerAuth)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme(bearer)
                                    .bearerFormat(format)
                    )
            );
  }
}
