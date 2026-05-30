package com.tumbesdemiercoles.api.category.application.usecase;

import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.application.ports.in.GetCategoryUseCase;
import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetCategoryUseCaseImpl implements GetCategoryUseCase {

  private final CategoryRepository categoryRepository;

  public Mono<CategoryResponseDto> getById(UUID id) {
    return categoryRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("Category", id)))
        .map(this::toResponse);
  }

  @Override
  public Mono<PageResponseDto<CategoryResponseDto>> findCategories(CategoryFilter filter) {
    return categoryRepository.findCategories(filter)
            .map(paginatedResult -> PageResponseDto.<CategoryResponseDto>builder()
                    .content(paginatedResult.getContent().stream().map(this::toResponse).toList())
                    .page(paginatedResult.getCurrentPage())
                    .size(paginatedResult.getPageSize())
                    .totalElements(paginatedResult.getTotalElements())
                    .totalPages(paginatedResult.getTotalPages())
                    .build());

  }

  @Override
  public Mono<List<CategoryResponseDto>> findAllActiveCategories() {
    return categoryRepository.findAllActive()
        .map(this::toResponse)
        .collectList();
  }

  private CategoryResponseDto toResponse(Category category) {
    return CategoryResponseDto.builder()
        .id(category.getId())
        .description(category.getDescription())
        .slug(category.getSlug())
        .isActive(category.getIsActive())
        .categoryId(category.getCategoryId())
        .build();
  }

}
