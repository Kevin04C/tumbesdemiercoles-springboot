package com.tumbesdemiercoles.api.common.application.dto;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponseDto {

  private List<NewsResponseDto> inCarousel;
  private List<NewsResponseDto> peruDailyNews;
  private List<NewsResponseDto> latestNews;
  private List<CategoryFeedItemDto> byCategory;
  private List<ColumnistResponseDto> columnists;
  private DigitalWeeklyResponseDto digitalWeekly;
}
