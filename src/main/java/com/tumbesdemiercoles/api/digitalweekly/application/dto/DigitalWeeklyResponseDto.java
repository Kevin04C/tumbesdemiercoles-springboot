package com.tumbesdemiercoles.api.digitalweekly.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para operaciones de DigitalWeekly.
 * Transporta datos desde los casos de uso hacia la capa de presentación.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalWeeklyResponseDto {

  private UUID id;
  private String pdfUrl;
  private String frontPageImageUrl;
  private String descripcion;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isPremium;
  private String url;
}
