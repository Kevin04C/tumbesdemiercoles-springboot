package com.tumbesdemiercoles.api.category.application.usecase;

import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.application.ports.in.GetCategoryTreeUseCase;
import com.tumbesdemiercoles.api.category.application.service.CategoryTreeBuilder;
import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoryTreeUseCaseImpl implements GetCategoryTreeUseCase {

  private final CategoryRepository categoryRepository;
  private final CategoryTreeBuilder treeBuilder;

  @Override
  public Mono<List<CategoryResponseDto>> execute() {
    return categoryRepository.findAll()
        .map(this::toResponse)
        .collectList()
        .map(treeBuilder::buildTree);
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
