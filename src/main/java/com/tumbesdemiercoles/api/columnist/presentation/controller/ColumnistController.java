package com.tumbesdemiercoles.api.columnist.presentation.controller;

import com.tumbesdemiercoles.api.columnist.application.ports.in.CreateColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.application.ports.in.DeleteColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.application.ports.in.GetColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.application.usecase.UpdateColumnistUseCaseImpl;
import com.tumbesdemiercoles.api.columnist.presentation.api.ColumnistControllerApi;
import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistFilterRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistUpdateRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.response.ColumnistResponse;
import com.tumbesdemiercoles.api.columnist.presentation.mapper.ColumnistWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/columnist")
@RequiredArgsConstructor
public class ColumnistController implements ColumnistControllerApi {
  private final CreateColumnistUseCase createColumnistUseCase;
  private final UpdateColumnistUseCaseImpl updateColumnistUseCase;
  private final DeleteColumnistUseCase deleteColumnistUseCase;
  private final GetColumnistUseCase getColumnistUseCase;
  private final ColumnistWebMapper webMapper;

  @Override
  public Mono<ApiResponse<ColumnistResponse>> createColumnist(ColumnistUpdateRequest request) {
    return createColumnistUseCase.execute(webMapper.toUpdateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Columnista creado correctamente"));
  }

  @Override
  public Mono<ApiResponse<List<ColumnistResponse>>> findColumnists(ColumnistFilterRequest filter) {
    return getColumnistUseCase.findColumnists(webMapper.toFilter(filter))
        .transform(webMapper::toPageResponse)
        .map(pageDto -> ApiResponse.success(pageDto, "Columnistas encontrados"));
  }


  @Override
  public Mono<ApiResponse<ColumnistResponse>> getColumnistById(UUID id) {
    return getColumnistUseCase.getById(id)
        .map(webMapper::toResponse)
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<ColumnistResponse>> updateColumnist(UUID id, ColumnistUpdateRequest request) {
    return updateColumnistUseCase.execute(id, webMapper.toUpdateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Columnista actualizado correctamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> deleteColumnist(UUID id) {
    return deleteColumnistUseCase.execute(id)
        .thenReturn(ApiResponse.success((Void) null, "Columnista eliminado correctamente"));
  }
}
