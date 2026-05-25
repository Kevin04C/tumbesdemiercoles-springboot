package com.tumbesdemiercoles.api.security.configuration;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Configura los filtros de seguridad web, la autenticación basada en JWT y las políticas de CORS.
 * Además, define los beans necesarios para la encriptación de contraseñas,
 * la decodificación de tokens JWT y el manejo de autenticación reactiva.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties({SecurityProps.class})
public class SecurityConfig {

  private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

  @Value("${jwt.secret}")
  private String secret;

  /**
   * Configura los filtros de seguridad para las solicitudes entrantes.
   * Desactiva CSRF, establece las configuraciones de CORS y define las reglas
   * de autorización para varias rutas.
   *
   * @param http Configuración de seguridad HTTP.
   * @return Un filtro de seguridad configurado para las rutas y autenticación de la aplicación.
   */
  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

    ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
    // Usamos el converter inyectado en la clase
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customJwtAuthenticationConverter);

    return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.addAllowedOriginPattern("*");
          config.addAllowedMethod("*");
          config.addAllowedHeader("*");
          config.setAllowCredentials(true);
          return config;
        }))
        .authorizeExchange(auth -> auth
            .pathMatchers("/v3/api-docs/**", "/swagger-ui.html", "/webjars/swagger-ui/**").permitAll()
                .pathMatchers(HttpMethod.POST,API_V1 + "/auth/register").permitAll()
                .pathMatchers(HttpMethod.POST,API_V1 + "/auth/login").permitAll()
                .pathMatchers("/actuator/health").permitAll()
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
        )
        .build();
  }
  /**
   * Configura un codificador de contraseñas utilizando BCrypt.
   *
   * @return Un bean de PasswordEncoder configurado con BCrypt.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configura un decodificador JWT basado en una clave secreta.
   *
   * @return Un bean de ReactiveJwtDecoder para procesar tokens JWT.
   */
  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    return NimbusReactiveJwtDecoder.withSecretKey(
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))).build();
  }

}
