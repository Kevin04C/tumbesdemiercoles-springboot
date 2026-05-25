package com.tumbesdemiercoles.api.category.application.ports.in;

import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GetCategoryTreeUseCase {
    Mono<List<CategoryResponseDto>> execute();
}
