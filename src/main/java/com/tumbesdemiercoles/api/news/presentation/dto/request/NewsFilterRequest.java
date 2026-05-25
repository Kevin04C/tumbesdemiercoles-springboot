package com.tumbesdemiercoles.api.news.presentation.dto.request;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
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
public class NewsFilterRequest extends BasePageRequest {
  private String slug;
  private String title;
  private Boolean isActive;
  private UUID categoryId;
  private Boolean isPremium;
  private Boolean isCarousel;
  private Boolean isPeruDailyNews;
  private Boolean isLatestNews;
}
