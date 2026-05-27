package com.tumbesdemiercoles.api.digitalweekly.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de dominio DigitalWeekly.
 * POJO puro sin anotaciones de frameworks.
 * Representa una edición semanal digital (PDF/imagen).
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DigitalWeekly {

  private UUID id;
  private String pdfUrl;
  private String frontPageImageUrl;
  private String descripcion;
  private Boolean isActive;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private String statusRegistry;
  private OffsetDateTime statusUpdatedAt;
  private Boolean isPremium;
  private String url;
}
