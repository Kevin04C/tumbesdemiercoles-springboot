package com.tumbesdemiercoles.api.category.application.ports.in;

import com.tumbesdemiercoles.api.category.domain.model.Category;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeleteCategoryUseCase {
    Mono<Void> execute(UUID id);
}
