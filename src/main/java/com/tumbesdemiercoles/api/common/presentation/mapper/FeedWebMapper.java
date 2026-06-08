package com.tumbesdemiercoles.api.common.presentation.mapper;

import com.tumbesdemiercoles.api.columnist.presentation.mapper.ColumnistWebMapper;
import com.tumbesdemiercoles.api.common.application.dto.CategoryFeedItemDto;
import com.tumbesdemiercoles.api.common.application.dto.FeedResponseDto;
import com.tumbesdemiercoles.api.common.presentation.dto.response.CategoryFeedItem;
import com.tumbesdemiercoles.api.common.presentation.dto.response.FeedResponse;
import com.tumbesdemiercoles.api.digitalweekly.presentation.mapper.DigitalWeeklyWebMapper;
import com.tumbesdemiercoles.api.news.presentation.mapper.NewsWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedWebMapper {

  private final NewsWebMapper newsWebMapper;
  private final ColumnistWebMapper columnistWebMapper;
  private final DigitalWeeklyWebMapper digitalWeeklyWebMapper;

  public FeedResponse toResponse(FeedResponseDto dto) {
    FeedResponse response = new FeedResponse();
    response.setInCarousel(dto.getInCarousel().stream().map(newsWebMapper::toResponse).toList());
    response.setPeruDailyNews(dto.getPeruDailyNews().stream().map(newsWebMapper::toResponse).toList());
    response.setByCategory(dto.getByCategory().stream().map(this::toCategoryItem).toList());
    response.setColumnists(dto.getColumnists().stream().map(columnistWebMapper::toResponse).toList());
    response.setDigitalWeekly(
        dto.getDigitalWeekly() != null
            ? digitalWeeklyWebMapper.toResponse(dto.getDigitalWeekly())
            : null
    );
    return response;
  }

  private CategoryFeedItem toCategoryItem(CategoryFeedItemDto dto) {
    CategoryFeedItem item = new CategoryFeedItem();
    item.setCategory(dto.getCategory());
    item.setSlug(dto.getSlug());
    item.setOrder(dto.getOrder());
    item.setNews(dto.getNews().stream().map(newsWebMapper::toResponse).toList());
    return item;
  }
}
