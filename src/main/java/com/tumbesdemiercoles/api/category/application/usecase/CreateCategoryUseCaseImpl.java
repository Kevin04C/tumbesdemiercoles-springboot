package com.tumbesdemiercoles.api.category.application.usecase;

import com.tumbesdemiercoles.api.category.application.dto.CategoryRequestDto;
import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.application.ports.in.CreateCategoryUseCase;
import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.shared.utils.SlugUtils;
import com.tumbesdemiercoles.api.user.application.ports.in.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {

  private final CategoryRepository categoryRepository;

  public Mono<CategoryResponseDto> execute(CategoryRequestDto dto) {
    Category category = Category.builder()
        .description(dto.getDescription())
        .slug(SlugUtils.toSlug(dto.getDescription()))
        .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
        .build();

    return categoryRepository.save(category)
        .map(this::toResponse);
  }

  private CategoryResponseDto toResponse(Category category) {
    return CategoryResponseDto.builder()
        .id(category.getId())
        .description(category.getDescription())
        .slug(category.getSlug())
        .isActive(category.getIsActive())
        .build();
  }

}
