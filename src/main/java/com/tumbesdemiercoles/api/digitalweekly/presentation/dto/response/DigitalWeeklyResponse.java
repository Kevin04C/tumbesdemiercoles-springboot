package com.tumbesdemiercoles.api.digitalweekly.presentation.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class DigitalWeeklyResponse {

  private UUID id;
  private String pdfUrl;
  private String frontPageImageUrl;
  private String descripcion;
  private Boolean isActive;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private Boolean isPremium;
  private String url;
}
