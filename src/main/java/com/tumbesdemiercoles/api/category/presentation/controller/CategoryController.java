package com.tumbesdemiercoles.api.category.presentation.controller;

import com.tumbesdemiercoles.api.category.application.ports.in.CreateCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.ports.in.DeleteCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.ports.in.GetCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.usecase.UpdateCategoryUseCaseImpl;
import com.tumbesdemiercoles.api.category.presentation.api.CategoryControllerApi;
import com.tumbesdemiercoles.api.category.presentation.mapper.CategoryWebMapper;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryFilterRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.response.CategoryResponse;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryUpdateRequest;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerApi {
  private final CreateCategoryUseCase createCategoryUseCase;
  private final UpdateCategoryUseCaseImpl updateCategoryUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;
  private final GetCategoryUseCase getCategoryUseCase;
  private final CategoryWebMapper webMapper;

  @Override
  public Mono<ApiResponse<PageResponseDto<CategoryResponse>>> findCategories(CategoryFilterRequest categoryFilterRequest) {
    return getCategoryUseCase.findCategories(
              webMapper.toFilter(categoryFilterRequest)
            )
            .map(webMapper::toPageResponse)
            .map(pageDto -> ApiResponse.success(pageDto));
  }

  @Override
  public Mono<ApiResponse<CategoryResponse>> getCategoryById(UUID id) {
      return getCategoryUseCase.getById(id)
              .map(webMapper::toResponse)
              .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<CategoryResponse>> updateCategory(UUID id, CategoryUpdateRequest categoryUpdateRequest) {
    return updateCategoryUseCase.execute(id, webMapper.toUpdateDto(categoryUpdateRequest))
            .map(webMapper::toResponse)
            .map(response -> ApiResponse.success(response, "Usuario Actualizado con correctamente"));
  }

  @Override
  public Mono<ApiResponse<CategoryResponse>> deleteCategory(UUID id) {
    return deleteCategoryUseCase.execute(id)
            .thenReturn(ApiResponse.success(null, "Categoria eliminada correctamente"));
  }
}
