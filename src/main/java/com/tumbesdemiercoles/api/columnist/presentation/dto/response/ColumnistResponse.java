package com.tumbesdemiercoles.api.columnist.presentation.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class ColumnistResponse {
  private UUID id;
  private String content;
  private String author;
  private String title;
  private String headline;
  private String authorImageUrl;
  private Boolean isActive;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
