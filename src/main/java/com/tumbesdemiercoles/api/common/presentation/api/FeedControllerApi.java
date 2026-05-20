package com.tumbesdemiercoles.api.common.presentation.api;

import com.tumbesdemiercoles.api.common.presentation.dto.response.FeedResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/feed")
public interface FeedControllerApi {

  @GetMapping
  Mono<ApiResponse<FeedResponse>> getFeed();
}
