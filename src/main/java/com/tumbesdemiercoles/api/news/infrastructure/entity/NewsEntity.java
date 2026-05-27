package com.tumbesdemiercoles.api.news.infrastructure.entity;

import java.util.UUID;

import com.tumbesdemiercoles.api.shared.infrastructure.entity.AuditableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@Table("news")
public class NewsEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("content")
  private String content;

  @Column("is_carousel")
  private Boolean isCarousel;

  @Column("headline")
  private String headline;

  @Column("slug")
  private String slug;

  @Column("is_premium")
  private Boolean isPremium;

  @Column("category_id")
  private UUID categoryId;

  @Column("title")
  private String title;

  @Column("is_active")
  private Boolean isActive;

  @Column("image_url")
  private String imageUrl;

  @Column("is_peru_daily_news")
  private Boolean isPeruDailyNews;

  @Column("is_latest_news")
  private Boolean isLatestNews;

}
