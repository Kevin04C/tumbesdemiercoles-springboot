package com.tumbesdemiercoles.api.security.utils;

import com.tumbesdemiercoles.api.security.configuration.EmailProps;
import com.tumbesdemiercoles.api.security.configuration.SecurityProps;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Utilidad para la generación, firma y validación de tokens JWT y HMAC.
 *
 * <p>Incluye lógica para:</p>
 * <ul>
 *   <li>Generar tokens de autenticación de usuario.</li>
 *   <li>Generar tokens de verificación de correo.</li>
 *   <li>Generar y parsear tokens de restablecimiento de contraseña con
 *       un “password fingerprint”.</li>
 *   <li>Verificar firmas HMAC-SHA256 en modo tiempo constante.</li>
 * </ul>
 *
 * <p>Se apoya en {@link EmailProps} para los TTL configurables de los tokens
 * y en {@link SecurityProps} para valores de seguridad adicionales (por ejemplo, pepper).</p>
 */
@Component
public class JwtUtil {

  private final String secret;
  private final EmailProps emailProps;  // para TTL reset
  private final SecurityProps securityProps;
  private static final String HMAC = "HmacSHA256";
  private static final String SCOPE_PWD_RESET = "pwd-reset";
  private static final String jwtSecret = "${jwt.secret}";
  private static final String scope = "scope";
  private static final String phf = "phf";
  private static final String hmacError = "HMAC error";
  private static final String errorPasswordFingerPrint = "Error al crear el Passwordfingerprint";
  private static final String badScope = "Bad scope";
  private static final String invalidSubject = "Invalid subject";
  private static final String missingFingerPrint = "Missing fingerprint";

  /**
   * Crea una nueva instancia de {@link JwtUtil}.
   *
   * @param secret        secreto JWT codificado en Base64, inyectado desde configuración
   * @param emailProps    propiedades para tiempos de vida de tokens relacionados con email
   * @param securityProps propiedades de seguridad adicionales (pepper, etc.)
   */
  public JwtUtil(
          @Value(jwtSecret) String secret,
          EmailProps emailProps, SecurityProps securityProps
  ) {
    this.secret = secret;
    this.emailProps = emailProps;
    this.securityProps = securityProps;
  }

  /**
   * Crea un token JWT genérico con sujeto, TTL y claims opcionales.
   *
   * @param user   sujeto del token (normalmente ID de usuario)
   * @param ttl    tiempo de vida del token
   * @param claims mapa de claims adicionales a incluir, puede ser {@code null}
   * @return token JWT firmado en formato compacto
   */
  private String createToken(String user, Duration ttl, Map<String, Object> claims) {

    Instant now = Instant.now();
    JwtBuilder builder = Jwts.builder()
            .subject(user)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(ttl)))
            .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)));

    if (claims != null) {
      claims.forEach(builder::claim);
    }
    return builder.compact();
  }

  /**
   * Genera un token JWT de autenticación para un usuario.
   *
   * <p>Incluye las autoridades (roles y permisos) del usuario en un claim {@code authorities}
   * y utiliza el TTL configurado en {@link EmailProps#getTokenUserTtlHours()}.</p>
   *
   * @param userData información principal del usuario autenticado
   * @return token JWT firmado
   */
  public String generateToken(UserPrincipal userData) {

    String user = userData.getId();

    Map<String, Object> claims = new HashMap<>();

    claims.put("roles", userData.getRoles());

    return createToken(user, Duration.ofHours(emailProps.getTokenUserTtlHours()), claims);
  }

  /**
   * Genera un token JWT para verificación de correo electrónico.
   *
   * <p>El sujeto del token es el ID del usuario y el TTL proviene de
   * {@link EmailProps#getVerificationEmailTokenTtlHours()}.</p>
   *
   * @param userId id del usuario al que pertenece el token
   * @return token JWT firmado para verificación de email
   */
  public String generateEmailToken(UUID id) {
    return createToken(id.toString(), Duration.ofHours(
            emailProps.getVerificationEmailTokenTtlHours()), null);
  }

  /**
   * Genera un token JWT para restablecimiento de contraseña.
   *
   * <p>Incluye:</p>
   * <ul>
   *   <li>Scope {@code pwd-reset} en la claim {@code scope}.</li>
   *   <li>Un “password fingerprint” en la claim {@code phf}, derivado
   *       del hash de la contraseña actual y un pepper configurado.</li>
   * </ul>
   *
   * @param userId             id del usuario
   * @param currentPasswordHash hash de la contraseña actual del usuario
   * @return token JWT firmado para restablecer contraseña
   */
  public String generatePasswordResetToken(Long id, String currentPasswordHash) {
    String fingerprint = generatePasswordFingerprint(currentPasswordHash);
    String user = String.valueOf(id);
    Map<String, Object> claims = new HashMap<>();
    claims.put(scope, SCOPE_PWD_RESET);
    claims.put(phf, fingerprint);

    return createToken(user, Duration.ofMinutes(
            emailProps.getResetPasswordTokenTtlMinutes()), claims);
  }

  /**
   * Parsea y valida un token JWT devolviendo sus claims.
   *
   * @param token token JWT en formato compacto
   * @return claims del token
   * @throws JwtException si el token es inválido o la firma no coincide
   */
  private Claims jwtParse(String token) {
    return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  /**
   * Crea un HMAC-SHA256 a partir de un secreto y un payload.
   *
   * @param secret  secreto utilizado para el HMAC
   * @param payload datos a firmar
   * @return arreglo de bytes con el resultado del HMAC
   * @throws IllegalStateException si ocurre un error al generar el HMAC
   */
  private byte[] createHmacSha256(byte[] secret, byte[] payload) {
    try {
      Mac mac = Mac.getInstance(HMAC);
      SecretKeySpec keySpec = new SecretKeySpec(secret, HMAC);
      mac.init(keySpec);
      byte[] hmacResult = mac.doFinal(payload);
      return hmacResult;
    } catch (Exception e) {
      throw new IllegalStateException(hmacError, e);
    }
  }

  /**
   * Extrae el sujeto (ID de usuario) de un token de email.
   *
   * @param token token JWT de verificación de email
   * @return ID de usuario contenido en el subject del token
   */
  public UUID parseEmailSubject(String token) {
    String subject = jwtParse(token).getSubject();
    return UUID.fromString(subject);
  }

  /**
   * Genera un “password fingerprint” usando HMAC-SHA256 sobre el hash
   * de la contraseña y un pepper configurable.
   *
   * @param passwordHash hash de la contraseña del usuario
   * @return cadena Base64 que representa el fingerprint
   * @throws IllegalStateException si ocurre algún error al generarlo
   */
  private String generatePasswordFingerprint(String passwordHash) {
    try {
      String pepper = securityProps.getResetPepper();
      byte[] pepperBytes = pepper.getBytes(StandardCharsets.UTF_8);
      byte[] passwordBytes = passwordHash.getBytes(StandardCharsets.UTF_8);

      byte[] hmacResult = createHmacSha256(pepperBytes, passwordBytes);
      return Base64.getEncoder().encodeToString(hmacResult);
    } catch (Exception e) {
      throw new IllegalStateException(errorPasswordFingerPrint, e);
    }
  }

  /**
   * Representa el resultado del parseo de un token de restablecimiento de contraseña.
   *
   * <p>Incluye el ID del usuario y el password fingerprint contenido en el token.</p>
   *
   * @param userId ID del usuario
   * @param phf    password fingerprint
   */
  public record ParsedReset(Long id, String phf) {
  }

  /**
   * Parsea y valida un token de restablecimiento de contraseña.
   *
   * <p>Realiza las siguientes validaciones:</p>
   * <ul>
   *   <li>Que el {@code scope} coincida con {@code pwd-reset}.</li>
   *   <li>Que el subject sea un ID numérico válido.</li>
   *   <li>Que exista un fingerprint no vacío en la claim {@code phf}.</li>
   * </ul>
   *
   * @param token token JWT de restablecimiento de contraseña
   * @return objeto {@link ParsedReset} con id y fingerprint
   * @throws JwtException si el scope es incorrecto, el subject no es válido
   *                      o falta el fingerprint
   */
  public ParsedReset parsePasswordResetToken(String token) {
    Claims claims = jwtParse(token);
    String scopes = claims.get(scope, String.class);

    if (!SCOPE_PWD_RESET.equals(scopes)) {
      throw new JwtException(badScope);
    }

    String sub = claims.getSubject();
    final Long id;
    try {
      id = Long.valueOf(sub);
    } catch (NumberFormatException e) {
      throw new JwtException(invalidSubject, e);
    }

    String fingerprint = claims.get(phf, String.class);
    if (fingerprint == null || fingerprint.isBlank()) {
      throw new JwtException(missingFingerPrint);
    }
    return new ParsedReset(id, fingerprint);
  }

  /**
   * Verifica una firma HMAC-SHA256 en formato hexadecimal para un payload dado,
   * utilizando un secreto proporcionado.
   *
   * <p>La comparación de firmas se realiza en tiempo constante para reducir
   * el riesgo de ataques de timing.</p>
   *
   * @param secret    secreto utilizado para generar el HMAC
   * @param payload   contenido sobre el que se genera/verifica el HMAC
   * @param signature firma esperada en formato hexadecimal
   * @return {@code true} si la firma calculada coincide, {@code false} en caso contrario
   */
  public boolean verifyHmacSha256(String secret, String payload, String signature) {
    if (signature == null || signature.isBlank()) {
      return false;
    }
    try {
      byte[] hashBytes = createHmacSha256(secret.getBytes(
              StandardCharsets.UTF_8), payload.getBytes(StandardCharsets.UTF_8));
      String computed = Hex.encodeHexString(hashBytes);
      return constantTimeEquals(computed, signature);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Compara dos cadenas en tiempo constante para evitar filtraciones
   * de información por diferencias de tiempo de ejecución.
   *
   * @param a primera cadena
   * @param b segunda cadena
   * @return {@code true} si ambas cadenas son iguales, {@code false} en caso contrario
   */
  private boolean constantTimeEquals(String a, String b) {
    if (a == null || b == null || a.length() != b.length()) {
      return false;
    }
    int r = 0;
    for (int i = 0; i < a.length(); i++) {
      r |= a.charAt(i) ^ b.charAt(i);
    }
    return r == 0;
  }

  /**
   * Extrae el ID del usuario (Subject) del token de autenticación.
   */
  public String extractUserId(String token) {
    return jwtParse(token).getSubject();
  }

  /**
   * Extrae la lista de Nombres de Roles contenidos en el token.
   */
  @SuppressWarnings("unchecked")
  public java.util.List<String> extractRoles(String token) {
    return jwtParse(token).get("roles", java.util.List.class);
  }
}


