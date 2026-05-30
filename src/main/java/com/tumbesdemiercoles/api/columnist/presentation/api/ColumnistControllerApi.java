package com.tumbesdemiercoles.api.columnist.presentation.api;

import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistFilterRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistUpdateRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.response.ColumnistResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Tag(name = "Columnist", description = "Endpoints para la publicación, consulta y administración de columnas de opinión de columnistas")
@RequestMapping("/api/v1/columnist")
public interface ColumnistControllerApi {

    @Operation(summary = "Crear columna de opinión", description = "Publica una nueva columna escrita por un columnista.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<ApiResponse<ColumnistResponse>> createColumnist(@Valid @RequestBody ColumnistUpdateRequest request);

    @Operation(summary = "Listar columnas de opinión", description = "Retorna una página de columnas de columnistas con filtros de búsqueda.")
    @GetMapping
    Mono<ApiResponse<List<ColumnistResponse>>> findColumnists(@Valid ColumnistFilterRequest filter);

    @Operation(summary = "Obtener columna por ID", description = "Recupera la información y contenido detallado de una columna por su ID único.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<ColumnistResponse>> getColumnistById(
        @Parameter(description = "ID único de la columna de opinión", required = true) @PathVariable UUID id);

    @Operation(summary = "Obtener columna por slug", description = "Recupera una columna de opinión por su slug único.")
    @GetMapping("/slug/{slug}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<ColumnistResponse>> getColumnistBySlug(
        @Parameter(description = "Slug único de la columna de opinión", required = true) @PathVariable String slug);

    @Operation(summary = "Actualizar columna de opinión", description = "Modifica el título, contenido u otros datos de una columna existente identificada por su ID.")
    @PutMapping("/{id}")
    Mono<ApiResponse<ColumnistResponse>> updateColumnist(
        @Parameter(description = "ID de la columna a actualizar", required = true) @PathVariable UUID id,
        @Valid @RequestBody ColumnistUpdateRequest request
    );

    @Operation(summary = "Eliminar columna de opinión", description = "Remueve una columna de opinión por su ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<Void>> deleteColumnist(
        @Parameter(description = "ID de la columna a eliminar", required = true) @PathVariable UUID id);
}

