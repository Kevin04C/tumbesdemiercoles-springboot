package com.tumbesdemiercoles.api.category.presentation.controller;

import com.tumbesdemiercoles.api.category.application.ports.in.CreateCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.ports.in.DeleteCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.ports.in.GetCategoryTreeUseCase;
import com.tumbesdemiercoles.api.category.application.ports.in.GetCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.usecase.*;
import com.tumbesdemiercoles.api.category.presentation.api.CategoryControllerApi;
import com.tumbesdemiercoles.api.category.presentation.mapper.CategoryWebMapper;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryFilterRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.response.CategoryResponse;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryUpdateRequest;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerApi {
  // private final CreateCategoryUseCaseImpl createCategoryUseCase;
  private final UpdateCategoryUseCaseImpl updateCategoryUseCase;
  private final DeleteCategoryUseCaseImpl deleteCategoryUseCase;
  private final GetCategoryUseCaseImpl getCategoryUseCase;
  private final GetCategoryTreeUseCaseImpl getCategoryTreeUseCase;
  private final CategoryWebMapper webMapper;

  @Override
  public Mono<ApiResponse<List<CategoryResponse>>> findCategories(CategoryFilterRequest categoryFilterRequest) {
    return getCategoryUseCase.findCategories(
              webMapper.toFilter(categoryFilterRequest)
            )
            .transform(webMapper::toPageResponse)
            .map(pageDto -> ApiResponse.success(pageDto, "Categorías encontradas"));
  }


  @Override
  public Mono<ApiResponse<List<CategoryResponse>>> findCategoryTree() {
    return getCategoryTreeUseCase.execute()
        .map(webMapper::toListResponse)
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<List<CategoryResponse>>> findAllActiveCategories() {
    return getCategoryUseCase.findAllActiveCategories()
        .map(webMapper::toListResponse)
        .map(response -> ApiResponse.success(response, "Categorías activas encontradas"));
  }

  @Override
  public Mono<ApiResponse<CategoryResponse>> getCategoryById(UUID id) {
      return getCategoryUseCase.getById(id)
              .map(webMapper::toResponse)
              .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<CategoryResponse>> getCategoryBySlug(String slug) {
    return getCategoryUseCase.getBySlug(slug)
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
  public Mono<ApiResponse<Void>> deleteCategory(UUID id) {
    return deleteCategoryUseCase.execute(id)
            .thenReturn(ApiResponse.success((Void) null, "Categoria eliminada correctamente"));
  }
}
