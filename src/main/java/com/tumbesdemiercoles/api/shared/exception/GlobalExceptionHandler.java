package com.tumbesdemiercoles.api.shared.exception;

import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Manejador global de excepciones para la API.
 * Captura excepciones y las convierte en respuestas ApiResponse consistentes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public Mono<ResponseEntity<ApiResponse<Void>>> handleResourceNotFound(
      ResourceNotFoundException ex, ServerWebExchange exchange) {
    log.warn("Resource not found: {}", ex.getMessage());
    return Mono.just(ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error(ex.getMessage(), "RESOURCE_NOT_FOUND", ex.getDetails())));
  }

  @ExceptionHandler(BadRequestException.class)
  public Mono<ResponseEntity<ApiResponse<Void>>> handleBadRequest(
      BadRequestException ex, ServerWebExchange exchange) {
    log.warn("Bad request: {}", ex.getMessage());
    return Mono.just(ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error(ex.getMessage(), "BAD_REQUEST", ex.getDetails())));
  }

  @ExceptionHandler(RuntimeException.class)
  public Mono<ResponseEntity<ApiResponse<Void>>> handleRuntimeException(
      RuntimeException ex, ServerWebExchange exchange) {
    log.error("Runtime exception: {}", ex.getMessage(), ex);
    return Mono.just(ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR", ex.getMessage())));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ApiResponse<Void>>> handleGenericException(
      Exception ex, ServerWebExchange exchange) {
    log.error("Unexpected exception: {}", ex.getMessage(), ex);
    return Mono.just(ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR", null)));
  }

}
