package com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalWeeklyUpdateRequest {

  private String pdfUrl;
  private String frontPageImageUrl;
  private String descripcion;
  private Boolean isActive;
  private Boolean isPremium;
  private String url;
}
