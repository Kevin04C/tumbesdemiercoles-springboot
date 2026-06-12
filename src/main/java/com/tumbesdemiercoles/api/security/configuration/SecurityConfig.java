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
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
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
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Value("${jwt.secret}")
  private String secret;

  private static final String API_V1 = "/api/v1";

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
            .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                .pathMatchers(HttpMethod.POST,API_V1 + "/auth/register").permitAll()
                .pathMatchers(HttpMethod.POST,API_V1 + "/auth/login").permitAll()
                .pathMatchers(HttpMethod.POST, API_V1 + "/auth/refresh").permitAll()

                // GETS
                .pathMatchers(HttpMethod.GET, API_V1 + "/feed").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/news/latest").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/news/{id:[a-f0-9-]+}/related").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/digital-weekly/latest").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/digital-weekly/{id:[a-f0-9-]+}").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/digital-weekly").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/categories/slug/{slug:[a-z0-9-]+}").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/news/by-category/{categorySlug:[a-z0-9-]+}").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/news/slug/{slug:[a-z0-9-]+}").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/columnist").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/columnist/slug/{slug:[a-z0-9-]+}").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/sitemap/general").permitAll()
                .pathMatchers(HttpMethod.GET, API_V1 + "/sitemap/news").permitAll()
                .pathMatchers(HttpMethod.POST, API_V1 + "/admin/reindex").permitAll()
                .pathMatchers("/actuator/health").permitAll()
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
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
    NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withSecretKey(
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))).build();

    OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();
    OAuth2TokenValidator<Jwt> typeValidator = new JwtClaimValidator<>(
        "token_type",
        claimValue -> "access".equals(claimValue)
    );
    OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(timestampValidator, typeValidator);
    jwtDecoder.setJwtValidator(validator);
    return jwtDecoder;
  }

}
