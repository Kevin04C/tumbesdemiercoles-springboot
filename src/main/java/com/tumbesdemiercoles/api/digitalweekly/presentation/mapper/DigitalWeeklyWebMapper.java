package com.tumbesdemiercoles.api.digitalweekly.presentation.mapper;

import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyRequestDto;
import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeeklyFilter;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyFilterRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyUpdateRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.response.DigitalWeeklyResponse;
import com.tumbesdemiercoles.api.shared.presentation.mapper.PageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DigitalWeeklyWebMapper extends PageMapper<DigitalWeeklyResponseDto, DigitalWeeklyResponse> {

  DigitalWeeklyRequestDto toCreateDto(DigitalWeeklyRequest request);

  DigitalWeeklyRequestDto toUpdateDto(DigitalWeeklyUpdateRequest request);

  @Mapping(target = "page", expression = "java(request.getOffsetPage())")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "sortBy", source = "sortBy")
  @Mapping(target = "sortDirection", source = "sortDir")
  DigitalWeeklyFilter toFilter(DigitalWeeklyFilterRequest request);
}
