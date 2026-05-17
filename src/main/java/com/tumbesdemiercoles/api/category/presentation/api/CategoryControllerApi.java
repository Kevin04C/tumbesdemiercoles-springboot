package com.tumbesdemiercoles.api.category.presentation.api;


import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryFilterRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.response.CategoryResponse;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryUpdateRequest;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/category")
public interface CategoryControllerApi {
    @GetMapping
    Mono<ApiResponse<PageResponseDto<CategoryResponse>>>findCategories(@Valid CategoryFilterRequest filter);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable UUID id
    );

    @PutMapping("/{id}")
    Mono<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<CategoryResponse>> deleteCategory(
            @PathVariable UUID id
    );

}
