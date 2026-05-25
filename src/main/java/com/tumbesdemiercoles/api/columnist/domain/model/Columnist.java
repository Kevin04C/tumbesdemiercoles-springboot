package com.tumbesdemiercoles.api.columnist.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Columnist {

  private UUID id;
  private String content;
  private String author;
  private String title;
  private String headline;
  private String authorImageUrl;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String statusRegistry;
  private LocalDateTime statusUpdatedAt;
}
