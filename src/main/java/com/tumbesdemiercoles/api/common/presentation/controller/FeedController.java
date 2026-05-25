package com.tumbesdemiercoles.api.common.presentation.controller;

import com.tumbesdemiercoles.api.common.application.usecase.GetFeedUseCase;
import com.tumbesdemiercoles.api.common.presentation.api.FeedControllerApi;
import com.tumbesdemiercoles.api.common.presentation.dto.response.FeedResponse;
import com.tumbesdemiercoles.api.common.presentation.mapper.FeedWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class FeedController implements FeedControllerApi {

  private final GetFeedUseCase getFeedUseCase;
  private final FeedWebMapper feedWebMapper;

  @Override
  public Mono<ApiResponse<FeedResponse>> getFeed() {
    return getFeedUseCase.execute()
        .map(feedWebMapper::toResponse)
        .map(ApiResponse::success);
  }
}
