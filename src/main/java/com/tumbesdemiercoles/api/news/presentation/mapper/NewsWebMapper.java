package com.tumbesdemiercoles.api.news.presentation.mapper;

import com.tumbesdemiercoles.api.news.application.dto.NewsRequestDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.application.dto.RelatedNewsResponseDto;
import com.tumbesdemiercoles.api.news.domain.model.NewsFilter;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsByCategorySlugFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsUpdateRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.response.NewsResponse;
import com.tumbesdemiercoles.api.news.presentation.dto.response.RelatedNewsResponse;
import com.tumbesdemiercoles.api.shared.presentation.mapper.PageMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsWebMapper extends PageMapper<NewsResponseDto, NewsResponse> {

  NewsRequestDto toCreateDto(NewsRequest request);

  NewsRequestDto toUpdateDto(NewsUpdateRequest request);

  @Mapping(target = "page", expression = "java(request.getOffsetPage())")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "sortBy", source = "sortBy")
  @Mapping(target = "sortDirection", source = "sortDir")
  NewsFilter toFilter(NewsFilterRequest request);

  @Mapping(target = "page", expression = "java(request.getOffsetPage())")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "sortBy", source = "sortBy")
  @Mapping(target = "sortDirection", source = "sortDir")
  NewsFilter toFilterByCategorySlug(NewsByCategorySlugFilterRequest request);

  RelatedNewsResponse toRelatedResponse(RelatedNewsResponseDto dto);

  List<RelatedNewsResponse> toRelatedResponseList(List<RelatedNewsResponseDto> dtos);
}
