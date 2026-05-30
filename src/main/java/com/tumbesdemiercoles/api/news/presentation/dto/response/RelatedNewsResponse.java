package com.tumbesdemiercoles.api.news.presentation.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class RelatedNewsResponse {
  private UUID id;
  private String title;
  private String slug;
  private String headline;
  private String imageUrl;
  private OffsetDateTime createdAt;
}
