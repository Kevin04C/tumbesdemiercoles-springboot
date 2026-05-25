package com.tumbesdemiercoles.api.digitalweekly.presentation.api;

import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyFilterRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyUpdateRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.response.DigitalWeeklyResponse;
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

@Tag(name = "Digital Weekly", description = "Endpoints para la publicación y descarga de ediciones de semanarios digitales (PDFs)")
@RequestMapping("/api/v1/digital-weekly")
public interface DigitalWeeklyControllerApi {

  @Operation(summary = "Subir edición de semanario digital", description = "Registra una nueva edición semanal con su archivo PDF, imagen de portada y descripción.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<DigitalWeeklyResponse>> createDigitalWeekly(
      @Valid @RequestBody DigitalWeeklyRequest request);

  @Operation(summary = "Listar ediciones del semanario", description = "Retorna una página con las ediciones del semanario digital ordenadas por fecha.")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<DigitalWeeklyResponse>>> findDigitalWeeklies(
      @Valid DigitalWeeklyFilterRequest filter);

  @Operation(summary = "Obtener edición por ID", description = "Recupera la información de una edición del semanario digital por su ID único.")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<DigitalWeeklyResponse>> getDigitalWeeklyById(
      @Parameter(description = "ID de la edición del semanario", required = true) @PathVariable UUID id);

  @Operation(summary = "Actualizar edición del semanario", description = "Permite modificar los enlaces de descarga, portada o descripción de una edición existente.")
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<DigitalWeeklyResponse>> updateDigitalWeekly(
      @Parameter(description = "ID de la edición a actualizar", required = true) @PathVariable UUID id,
      @Valid @RequestBody DigitalWeeklyUpdateRequest request);

  @Operation(summary = "Eliminar edición del semanario", description = "Remueve una edición semanal por su ID.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deleteDigitalWeekly(
      @Parameter(description = "ID de la edición a eliminar", required = true) @PathVariable UUID id);
}

