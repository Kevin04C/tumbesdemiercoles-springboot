package com.tumbesdemiercoles.api.digitalweekly.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para operaciones de DigitalWeekly.
 * Transporta datos desde la capa de presentación hacia los casos de uso.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalWeeklyRequestDto {
  private String pdfUrl;
  private String frontPageImageUrl;
  private String descripcion;
  private Boolean isActive;
  private Boolean isPremium;
  private String url;
}
