package com.tumbesdemiercoles.api.common.presentation.dto.response;

import com.tumbesdemiercoles.api.columnist.presentation.dto.response.ColumnistResponse;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.response.DigitalWeeklyResponse;
import com.tumbesdemiercoles.api.news.presentation.dto.response.NewsResponse;
import lombok.Data;

import java.util.List;

@Data
public class FeedResponse {

  private List<NewsResponse> inCarousel;
  private List<NewsResponse> peruDailyNews;
  private List<CategoryFeedItem> byCategory;
  private List<ColumnistResponse> columnists;
  private DigitalWeeklyResponse digitalWeekly;
}
