package com.tumbesdemiercoles.api.news.presentation.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpdateRequest {

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
}
