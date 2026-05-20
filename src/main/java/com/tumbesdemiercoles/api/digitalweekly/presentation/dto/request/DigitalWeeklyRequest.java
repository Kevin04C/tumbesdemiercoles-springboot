package com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalWeeklyRequest {

  private String pdfUrl;

  private String frontPageImageUrl;

  @NotBlank(message = "La descripción no puede estar vacía")
  private String descripcion;

  private Boolean isActive;

  private Boolean isPremium;

  private String url;
}
