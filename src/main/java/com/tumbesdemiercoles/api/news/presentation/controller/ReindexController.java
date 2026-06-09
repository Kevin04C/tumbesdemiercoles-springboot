package com.tumbesdemiercoles.api.news.presentation.controller;

import com.tumbesdemiercoles.api.news.application.ports.in.ReindexUseCase;
import com.tumbesdemiercoles.api.shared.exception.UnauthorizedException;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class ReindexController {

  private final ReindexUseCase reindexUseCase;

  @Value("${admin.api-key}")
  private String adminApiKey;

  @PostMapping("/reindex")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<ApiResponse<Void>> reindex(@RequestHeader("X-API-Key") String apiKey) {
    if (!adminApiKey.equals(apiKey)) {
      return Mono.error(UnauthorizedException.forInvalidCredentials());
    }
    return reindexUseCase.execute()
        .thenReturn(emptyResponse());
  }

  private static ApiResponse<Void> emptyResponse() {
    return ApiResponse.<Void>builder()
        .success(true)
        .message("Reindexado iniciado correctamente")
        .build();
  }
}
