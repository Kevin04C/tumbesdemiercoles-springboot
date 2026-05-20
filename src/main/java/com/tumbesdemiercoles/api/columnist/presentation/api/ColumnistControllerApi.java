package com.tumbesdemiercoles.api.columnist.presentation.api;

import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistFilterRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.request.ColumnistUpdateRequest;
import com.tumbesdemiercoles.api.columnist.presentation.dto.response.ColumnistResponse;
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

@RequestMapping("/api/v1/columnist")
public interface ColumnistControllerApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<ApiResponse<ColumnistResponse>> createColumnist(@Valid @RequestBody ColumnistUpdateRequest request);

    @GetMapping
    Mono<ApiResponse<PageResponseDto<ColumnistResponse>>> findColumnists(@Valid ColumnistFilterRequest filter);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<ColumnistResponse>> getColumnistById(@PathVariable UUID id);

    @PutMapping("/{id}")
    Mono<ApiResponse<ColumnistResponse>> updateColumnist(
        @PathVariable UUID id,
        @Valid @RequestBody ColumnistUpdateRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ApiResponse<ColumnistResponse>> deleteColumnist(@PathVariable UUID id);
}
