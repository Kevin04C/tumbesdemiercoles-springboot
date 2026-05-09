package com.tumbesdemiercoles.api.category.application.usecase;

import com.tumbesdemiercoles.api.category.application.dto.CategoryRequestDto;
import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase {

  private final CategoryRepository categoryRepository;

  public Mono<CategoryResponseDto> execute(UUID id, CategoryRequestDto dto) {
    return categoryRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("Category", id)))
        .map(existing -> Category.builder()
            .id(existing.getId())
            .description(dto.getDescription() != null ? dto.getDescription() : existing.getDescription())
            .isActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive())
            .statusRegistry(existing.getStatusRegistry())
            .build())
        .flatMap(categoryRepository::save)
        .map(this::toResponse);
  }

  private CategoryResponseDto toResponse(Category category) {
    return CategoryResponseDto.builder()
        .id(category.getId())
        .description(category.getDescription())
        .isActive(category.getIsActive())
        .build();
  }

}
