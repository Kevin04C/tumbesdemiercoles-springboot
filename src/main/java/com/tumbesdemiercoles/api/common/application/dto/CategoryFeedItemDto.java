package com.tumbesdemiercoles.api.common.application.dto;

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
public class CategoryFeedItemDto {

  private String category;
  private List<NewsResponseDto> news;
}
