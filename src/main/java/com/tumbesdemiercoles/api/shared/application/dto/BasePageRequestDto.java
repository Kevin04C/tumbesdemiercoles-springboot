package com.tumbesdemiercoles.api.shared.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasePageRequestDto {

  @Builder.Default
  private int page = 0;

  @Builder.Default
  private int size = 10;

  @Builder.Default
  private String sortBy = "id";

  @Builder.Default
  private String sortDirection = "asc";
}