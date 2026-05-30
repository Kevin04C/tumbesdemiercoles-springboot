package com.tumbesdemiercoles.api.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tumbesdemiercoles.api.shared.response.ApiError;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.time.OffsetDateTime;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public CustomAccessDeniedHandler() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ex) {
    var response = exchange.getResponse();
    response.setStatusCode(HttpStatus.FORBIDDEN);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    var body = ApiResponse.<Void>builder()
        .success(false)
        .message("Access denied")
        .error(ApiError.builder()
            .code("FORBIDDEN")
            .details("You do not have permission to access this resource.")
            .build())
        .timestamp(OffsetDateTime.now())
        .build();

    try {
      byte[] bytes = objectMapper.writeValueAsBytes(body);
      DataBuffer buffer = response.bufferFactory().wrap(bytes);
      return response.writeWith(Mono.just(buffer));
    } catch (Exception e) {
      return Mono.error(e);
    }
  }
}
