package com.tumbesdemiercoles.api.common.presentation.dto.response;

import com.tumbesdemiercoles.api.news.presentation.dto.response.NewsResponse;
import lombok.Data;

import java.util.List;

@Data
public class CategoryFeedItem {

  private String category;
  private String slug;
  private List<NewsResponse> news;
}
