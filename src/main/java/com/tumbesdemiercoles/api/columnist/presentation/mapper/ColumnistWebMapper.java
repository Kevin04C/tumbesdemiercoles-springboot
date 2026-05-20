package com.tumbesdemiercoles.api.columnist.presentation.mapper;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistRequestDto;
import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.columnist.domain.model.ColumnistFilter;
import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistFilterRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistUpdateRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.response.ColumnistResponse;
import com.tumbesdemiercoles.api.shared.presentation.mapper.PageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColumnistWebMapper extends PageMapper<ColumnistResponseDto, ColumnistResponse> {

    ColumnistRequestDto toUpdateDto(ColumnistUpdateRequest request);

    @Mapping(target = "page", expression = "java(request.getOffsetPage())")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "sortBy", source = "sortBy")
    @Mapping(target = "sortDirection", source = "sortDir")
    ColumnistFilter toFilter(ColumnistFilterRequest request);
}
