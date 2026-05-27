package com.tumbesdemiercoles.api.news.presentation.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class NewsResponse {

  private UUID id;
  private String content;
  private Boolean isCarousel;
  private String headline;
  private String slug;
  private Boolean isPremium;
  private UUID categoryId;
  private String title;
  private Boolean isActive;
  private String imageUrl;
  private Boolean isPeruDailyNews;
  private Boolean isLatestNews;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
