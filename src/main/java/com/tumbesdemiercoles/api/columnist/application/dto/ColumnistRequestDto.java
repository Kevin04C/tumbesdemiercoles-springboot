package com.tumbesdemiercoles.api.columnist.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnistRequestDto {
  private String content;
  private String author;
  private String title;
  private String headline;
  private String authorImageUrl;
  private Boolean isActive;
}
