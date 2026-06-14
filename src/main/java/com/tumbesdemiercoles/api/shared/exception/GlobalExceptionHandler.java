package com.tumbesdemiercoles.api.shared.exception;

import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Manejador global de excepciones para la API.
 * Captura excepciones y las convierte en respuestas ApiResponse consistentes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 1. RECURSO NO ENCONTRADO (404)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    return buildResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage(), ex.getDetails());
  }

  // 2. PETICIÓN INCORRECTA (400)
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
    log.warn("Bad request: {}", ex.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), ex.getDetails());
  }

  // 3. CONFLICTO DE REGLAS DE NEGOCIO (409) - Ej: Email ya registrado
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
    log.warn("Conflict: {}", ex.getMessage());
    return buildResponse(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), ex.getDetails());
  }

  // 4. NO AUTORIZADO (401) - Ej: Faltan credenciales o el token expiró
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
    log.warn("Unauthorized: {}", ex.getMessage());
    return buildResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), ex.getDetails());
  }

  // 5. PROHIBIDO (403) - Ej: Tiene token, pero no tiene el rol de ADMIN
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException ex) {
    log.warn("Forbidden: {}", ex.getMessage());
    return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage(), ex.getDetails());
  }

  // 6. ERRORES DE VALIDACIÓN (400) - Ej: @NotBlank, @Email en los DTOs fallan
  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(WebExchangeBindException ex) {
    // Extraemos todos los errores y los unimos en un solo mensaje claro
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .findFirst() // O puedes usar Collectors.joining(", ") si quieres mostrar todos a la vez
        .orElse("Error de validación en los datos enviados");

    log.warn("Validation error: {}", errorMessage);
    return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", errorMessage, null);
  }

  // 7. ERRORES INTERNOS (500) - Fallos inesperados (Base de datos caída, NullPointer, etc.)
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
    log.error("Runtime exception: {}", ex.getMessage(), ex);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Error interno del servidor",
        "Por favor, contacte a soporte si el error persiste.");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
    log.error("Unexpected exception: {}", ex.getMessage(), ex);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Error inesperado",
        "Por favor, contacte a soporte si el error persiste.");
  }
  // ===================================================================================
  // NUEVOS MANEJADORES PARA EL DOMINIO PURO
  // ===================================================================================

  // ARGUMENTOS INVÁLIDOS DEL DOMINIO (400) - Ej: Nombre vacío, precio negativo
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.warn("Illegal argument in domain: {}", ex.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_DATA", ex.getMessage(), null);
  }
  // 9. REGLAS DE NEGOCIO ROTAS EN EL DOMINIO (409) - Ej: Usuario inactivo, email ya verificado
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
    log.warn("Business rule violation: {}", ex.getMessage());
    return buildResponse(HttpStatus.CONFLICT, "BUSINESS_RULE_VIOLATION", ex.getMessage(), null);
  }

  // 10. ACCESO DENEGADO (403) - Spring Security
  @ExceptionHandler(AuthorizationDeniedException.class)
  public Mono<ResponseEntity<ApiResponse<Void>>> handleAuthorizationDenied(
      AuthorizationDeniedException ex, ServerWebExchange exchange) {
    return exchange.getPrincipal()
        .map(principal -> principal.getName())
        .defaultIfEmpty("Anónimo")
        .map(username -> {
          log.warn("Access Denied | User: {} | Path: {} {} | Reason: {}",
              username,
              exchange.getRequest().getMethod(),
              exchange.getRequest().getPath().value(),
              ex.getMessage());
          return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied", ex.getMessage());
        });
  }

  // ===================================================================================
  // MÉTODO AUXILIAR PARA NO REPETIR CÓDIGO (DRY - Don't Repeat Yourself)
  // ===================================================================================
  private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus status, String code, String message, String details) {
    return ResponseEntity
        .status(status)
        .body(ApiResponse.error(message, code, details));
  }

}