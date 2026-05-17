package com.tumbesdemiercoles.api.category.presentation.mapper;

import com.tumbesdemiercoles.api.category.application.dto.CategoryRequestDto;
import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryFilterRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryUpdateRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.response.CategoryResponse;
import com.tumbesdemiercoles.api.shared.presentation.mapper.PageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryWebMapper extends PageMapper<CategoryResponseDto, CategoryResponse> {

    CategoryRequestDto toUpdateDto(CategoryUpdateRequest request);

    @Mapping(target = "page", expression = "java(request.getOffsetPage())")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "sortBy", source = "sortBy")
    @Mapping(target = "sortDirection", source = "sortDir")
    CategoryFilter toFilter(CategoryFilterRequest request);
}
