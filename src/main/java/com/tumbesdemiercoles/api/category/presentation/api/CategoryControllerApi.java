package com.tumbesdemiercoles.api.category.presentation.api;

import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryFilterRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.request.CategoryUpdateRequest;
import com.tumbesdemiercoles.api.category.presentation.dto.response.CategoryResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Category", description = "Endpoints para la gestión y clasificación jerárquica de categorías de noticias")
@RequestMapping("/api/v1/categories")
public interface CategoryControllerApi {

    @Operation(summary = "Listar categorías", description = "Retorna una página de categorías filtradas por los parámetros de búsqueda.")
    @GetMapping
    Mono<ApiResponse<List<CategoryResponse>>> findCategories(@Valid CategoryFilterRequest filter);

    @Operation(summary = "Obtener árbol de categorías", description = "Recupera la estructura jerárquica de todas las categorías activas en formato de árbol.")
    @GetMapping("/tree")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<List<CategoryResponse>>> findCategoryTree();

    @Operation(summary = "Listar todas las categorías activas", description = "Retorna una lista plana con todas las categorías que se encuentran activas.")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<List<CategoryResponse>>> findAllActiveCategories();

    @Operation(summary = "Obtener categoría por ID", description = "Busca y retorna el detalle de una categoría específica por su identificador único.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "ID único de la categoría", required = true) @PathVariable UUID id
    );

    @Operation(summary = "Actualizar categoría", description = "Modifica los datos de una categoría identificada por su ID.")
    @PutMapping("/{id}")
    Mono<ApiResponse<CategoryResponse>> updateCategory(
            @Parameter(description = "ID de la categoría a actualizar", required = true) @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest
    );

    @Operation(summary = "Eliminar categoría", description = "Realiza la eliminación lógica de una categoría de noticias por su ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "ID de la categoría a eliminar", required = true) @PathVariable UUID id
    );

}

