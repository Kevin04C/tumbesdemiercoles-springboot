package com.tumbesdemiercoles.api.category.application.ports.in;

import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.category.presentation.dto.response.CategoryResponse;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GetCategoryUseCase {
    Mono<CategoryResponseDto> getById(UUID id);
    Mono<PageResponseDto<CategoryResponseDto>> findCategories(CategoryFilter filter);
}
