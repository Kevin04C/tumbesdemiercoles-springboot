package com.tumbesdemiercoles.api.news.infrastructure.mapper;

import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.infrastructure.entity.NewsEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsPersistenceMapper {

  public News toDomain(NewsEntity entity) {
    return News.builder()
        .id(entity.getId())
        .content(entity.getContent())
        .isCarousel(entity.getIsCarousel())
        .headline(entity.getHeadline())
        .slug(entity.getSlug())
        .isPremium(entity.getIsPremium())
        .categoryId(entity.getCategoryId())
        .title(entity.getTitle())
        .isActive(entity.getIsActive())
        .imageUrl(entity.getImageUrl())
        .isPeruDailyNews(entity.getIsPeruDailyNews())
        .isLatestNews(entity.getIsLatestNews())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .statusRegistry(entity.getStatusRegistry())
        .statusUpdatedAt(entity.getStatusUpdatedAt())
        .build();
  }

  public NewsEntity toEntity(News domain) {
    return NewsEntity.builder()
        .id(domain.getId())
        .content(domain.getContent())
        .isCarousel(domain.getIsCarousel())
        .headline(domain.getHeadline())
        .slug(domain.getSlug())
        .isPremium(domain.getIsPremium())
        .categoryId(domain.getCategoryId())
        .title(domain.getTitle())
        .isActive(domain.getIsActive())
        .imageUrl(domain.getImageUrl())
        .isPeruDailyNews(domain.getIsPeruDailyNews())
        .isLatestNews(domain.getIsLatestNews())
        .build();
  }
}
