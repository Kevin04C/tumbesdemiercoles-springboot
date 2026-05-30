package com.tumbesdemiercoles.api.columnist.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Columnist {

  private UUID id;
  private String content;
  private String author;
  private String title;
  private String slug;
  private String headline;
  private String authorImageUrl;
  private Boolean isActive;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private String statusRegistry;
  private OffsetDateTime statusUpdatedAt;
}
