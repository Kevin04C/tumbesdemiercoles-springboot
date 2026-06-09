package com.tumbesdemiercoles.api.news.infrastructure.search;

import com.tumbesdemiercoles.api.news.domain.model.News;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsSearchDocument {

  private String id;
  private String title;
  private String headline;
  private String slug;
  private String imageUrl;
  private String createdAt;
  private UUID categoryId;
  private boolean isActive;
  private boolean isPremium;
  private String content;

  public static NewsSearchDocument from(News news) {
    return NewsSearchDocument.builder()
        .id(news.getId().toString())
        .title(news.getTitle())
        .headline(news.getHeadline())
        .slug(news.getSlug())
        .imageUrl(news.getImageUrl())
        .createdAt(news.getCreatedAt() != null ? news.getCreatedAt().toString() : null)
        .categoryId(news.getCategoryId())
        .isActive(news.getIsActive() != null ? news.getIsActive() : false)
        .isPremium(news.getIsPremium() != null ? news.getIsPremium() : false)
        .content(news.getContent())
        .build();
  }
}
