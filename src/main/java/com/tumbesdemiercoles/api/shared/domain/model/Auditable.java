package com.tumbesdemiercoles.api.shared.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Auditable {
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private String statusRegistry;
  private OffsetDateTime statusUpdatedAt;

  public void setStatusRegistry(String newStatus) {
    if (!Objects.equals(this.statusRegistry, newStatus)) {
      this.statusRegistry = newStatus;
      this.statusUpdatedAt = OffsetDateTime.now();
    }
  }
}
