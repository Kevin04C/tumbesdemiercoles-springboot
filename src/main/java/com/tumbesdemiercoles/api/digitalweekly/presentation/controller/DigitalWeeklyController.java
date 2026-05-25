package com.tumbesdemiercoles.api.digitalweekly.presentation.controller;

import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.CreateDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.DeleteDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.GetDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.UpdateDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.presentation.api.DigitalWeeklyControllerApi;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyFilterRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyUpdateRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.response.DigitalWeeklyResponse;
import com.tumbesdemiercoles.api.digitalweekly.presentation.mapper.DigitalWeeklyWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DigitalWeeklyController implements DigitalWeeklyControllerApi {

  private final CreateDigitalWeeklyUseCase createDigitalWeeklyUseCase;
  private final UpdateDigitalWeeklyUseCase updateDigitalWeeklyUseCase;
  private final DeleteDigitalWeeklyUseCase deleteDigitalWeeklyUseCase;
  private final GetDigitalWeeklyUseCase getDigitalWeeklyUseCase;
  private final DigitalWeeklyWebMapper webMapper;

  @Override
  public Mono<ApiResponse<DigitalWeeklyResponse>> createDigitalWeekly(DigitalWeeklyRequest request) {
    return createDigitalWeeklyUseCase.execute(webMapper.toCreateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Edición semanal creada correctamente"));
  }

  @Override
  public Mono<ApiResponse<List<DigitalWeeklyResponse>>> findDigitalWeeklies(
      DigitalWeeklyFilterRequest filter) {
    return getDigitalWeeklyUseCase.findDigitalWeeklies(webMapper.toFilter(filter))
        .transform(webMapper::toPageResponse)
        .map(pageDto -> ApiResponse.success(pageDto, "Ediciones semanales encontradas"));
  }


  @Override
  public Mono<ApiResponse<DigitalWeeklyResponse>> getDigitalWeeklyById(UUID id) {
    return getDigitalWeeklyUseCase.getById(id)
        .map(webMapper::toResponse)
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<DigitalWeeklyResponse>> updateDigitalWeekly(
      UUID id, DigitalWeeklyUpdateRequest request) {
    return updateDigitalWeeklyUseCase.execute(id, webMapper.toUpdateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Edición semanal actualizada correctamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> deleteDigitalWeekly(UUID id) {
    return deleteDigitalWeeklyUseCase.execute(id)
        .thenReturn(ApiResponse.success((Void) null, "Edición semanal eliminada correctamente"));
  }
}
