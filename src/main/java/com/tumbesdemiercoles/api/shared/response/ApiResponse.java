package com.tumbesdemiercoles.api.shared.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para envolver todas las respuestas de la API.
 * Proporciona una estructura consistente para éxito y error.
 *
 * @param <T> Tipo de datos contenidos en la respuesta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private boolean success;
  private String message;
  private T data;
  private ApiError error;
  private PaginationInfo pagination;
  
  @Builder.Default
  private OffsetDateTime timestamp = OffsetDateTime.now();

  /**
   * Crea una respuesta exitosa con datos.
   */
  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
        .success(true)
        .message("Operation completed successfully")
        .data(data)
        .timestamp(OffsetDateTime.now())
        .build();
  }

  /**
   * Crea una respuesta exitosa con datos y mensaje personalizado.
   */
  public static <T> ApiResponse<T> success(T data, String message) {
    return ApiResponse.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .timestamp(OffsetDateTime.now())
        .build();
  }

  /**
   * Crea una respuesta exitosa con datos y paginación.
   */
  public static <T> ApiResponse<T> success(T data, PaginationInfo pagination) {
    return ApiResponse.<T>builder()
        .success(true)
        .message("Operation completed successfully")
        .data(data)
        .pagination(pagination)
        .timestamp(OffsetDateTime.now())
        .build();
  }

  /**
   * Crea una respuesta exitosa con datos, mensaje y paginación.
   */
  public static <T> ApiResponse<T> success(T data, String message, PaginationInfo pagination) {
    return ApiResponse.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .pagination(pagination)
        .timestamp(OffsetDateTime.now())
        .build();
  }

  /**
   * Crea una respuesta de error.
   */
  public static <T> ApiResponse<T> error(String message, String errorCode, String details) {
    return ApiResponse.<T>builder()
        .success(false)
        .message(message)
        .error(ApiError.builder()
            .code(errorCode)
            .details(details)
            .build())
        .timestamp(OffsetDateTime.now())
        .build();
  }

  /**
   * Crea una respuesta de error con solo mensaje.
   */
  public static <T> ApiResponse<T> error(String message) {
    return ApiResponse.<T>builder()
        .success(false)
        .message(message)
        .timestamp(OffsetDateTime.now())
        .build();
  }

  public static <T> ApiResponse<List<T>> success(PageResponseDto<T> page) {
    return ApiResponse.<List<T>>builder()
        .success(true)
        .message("Operation completed successfully")
        .data(page.getContent())
        .pagination(PaginationInfo.of(page.getPage(), page.getSize(), page.getTotalElements()))
        .timestamp(OffsetDateTime.now())
        .build();
  }

  public static <T> ApiResponse<List<T>> success(PageResponseDto<T> page, String message) {
    return ApiResponse.<List<T>>builder()
        .success(true)
        .message(message)
        .data(page.getContent())
        .pagination(PaginationInfo.of(page.getPage(), page.getSize(), page.getTotalElements()))
        .timestamp(OffsetDateTime.now())
        .build();
  }

}
