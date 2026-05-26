package com.tumbesdemiercoles.api.news.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class News {

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
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String statusRegistry;
  private LocalDateTime statusUpdatedAt;
}
