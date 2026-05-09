package com.tumbesdemiercoles.api.category.presentation.controller;

import com.tumbesdemiercoles.api.category.application.dto.CategoryRequestDto;
import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import com.tumbesdemiercoles.api.category.application.usecase.CreateCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.usecase.DeleteCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.usecase.GetCategoryUseCase;
import com.tumbesdemiercoles.api.category.application.usecase.UpdateCategoryUseCase;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CreateCategoryUseCase createCategoryUseCase;
  private final GetCategoryUseCase getCategoryUseCase;
  private final UpdateCategoryUseCase updateCategoryUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;

  @PostMapping
  public Mono<ResponseEntity<ApiResponse<CategoryResponseDto>>> createCategory(
      @RequestBody CategoryRequestDto request) {
    return createCategoryUseCase.execute(request)
        .map(result -> ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(result, "Categoría creada exitosamente")));
  }

  @GetMapping
  public Mono<ResponseEntity<ApiResponse<List<CategoryResponseDto>>>> getAllCategories() {
    return getCategoryUseCase.getAll()
        .collectList()
        .map(result -> ResponseEntity.ok(ApiResponse.success(result)));
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<ApiResponse<CategoryResponseDto>>> getCategoryById(@PathVariable UUID id) {
    return getCategoryUseCase.getById(id)
        .map(result -> ResponseEntity.ok(ApiResponse.success(result)));
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<ApiResponse<CategoryResponseDto>>> updateCategory(
      @PathVariable UUID id, @RequestBody CategoryRequestDto request) {
    return updateCategoryUseCase.execute(id, request)
        .map(result -> ResponseEntity.ok(ApiResponse.success(result, "Categoría actualizada exitosamente")));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<ApiResponse<Void>>> deleteCategory(@PathVariable UUID id) {
    return deleteCategoryUseCase.execute(id)
        .then(Mono.just(ResponseEntity.ok(ApiResponse.<Void>success(null, "Categoría eliminada exitosamente"))));
  }

}
