package com.tumbesdemiercoles.api.news.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestDto {
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
