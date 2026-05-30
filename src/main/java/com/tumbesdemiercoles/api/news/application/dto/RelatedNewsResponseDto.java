package com.tumbesdemiercoles.api.news.application.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
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
public class RelatedNewsResponseDto {
  private UUID id;
  private String title;
  private String slug;
  private String headline;
  private String imageUrl;
  private OffsetDateTime createdAt;
}
