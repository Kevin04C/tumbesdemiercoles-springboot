package com.tumbesdemiercoles.api.digitalweekly.presentation.api;

import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyFilterRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request.DigitalWeeklyUpdateRequest;
import com.tumbesdemiercoles.api.digitalweekly.presentation.dto.response.DigitalWeeklyResponse;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
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

@RequestMapping("/api/v1/digital-weekly")
public interface DigitalWeeklyControllerApi {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<DigitalWeeklyResponse>> createDigitalWeekly(
      @Valid @RequestBody DigitalWeeklyRequest request);

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PageResponseDto<DigitalWeeklyResponse>>> findDigitalWeeklies(
      @Valid DigitalWeeklyFilterRequest filter);

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<DigitalWeeklyResponse>> getDigitalWeeklyById(
      @PathVariable UUID id);

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<DigitalWeeklyResponse>> updateDigitalWeekly(
      @PathVariable UUID id,
      @Valid @RequestBody DigitalWeeklyUpdateRequest request);

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deleteDigitalWeekly(@PathVariable UUID id);
}
