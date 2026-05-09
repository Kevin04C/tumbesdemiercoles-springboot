package com.tumbesdemiercoles.api.shared.domain.model;

import java.time.LocalDateTime;
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
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String statusRegistry;
  private LocalDateTime statusUpdatedAt;

  public void setStatusRegistry(String newStatus) {
    if (!Objects.equals(this.statusRegistry, newStatus)) {
      this.statusRegistry = newStatus;
      this.statusUpdatedAt = LocalDateTime.now();
    }
  }
}
