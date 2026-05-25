package com.tumbesdemiercoles.api.news.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.BasePaginated;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewsFilter extends BasePaginated {
  private UUID id;
  private String slug;
  private String title;
  private Boolean isActive;
  private UUID categoryId;
  private Boolean isPremium;
  private Boolean isCarousel;
  private Boolean isPeruDailyNews;
  private Boolean isLatestNews;
}
