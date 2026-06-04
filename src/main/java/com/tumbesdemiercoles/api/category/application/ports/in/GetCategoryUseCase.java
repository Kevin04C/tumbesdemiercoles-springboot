package com.tumbesdemiercoles.api.category.application.ports.in;

import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetCategoryUseCase {
    Mono<CategoryResponseDto> getById(UUID id);
    Mono<CategoryResponseDto> getBySlug(String slug);
    Mono<PageResponseDto<CategoryResponseDto>> findCategories(CategoryFilter filter);
    Mono<List<CategoryResponseDto>> findAllActiveCategories();
}
