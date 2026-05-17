package com.tumbesdemiercoles.api.category.application.ports.in;

import com.tumbesdemiercoles.api.category.application.dto.CategoryRequestDto;
import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.domain.model.Category;
import reactor.core.publisher.Mono;

public interface CreateCategoryUseCase {
    Mono<CategoryResponseDto> execute(CategoryRequestDto category);
}
