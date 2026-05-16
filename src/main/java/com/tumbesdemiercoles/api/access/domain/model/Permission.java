package com.tumbesdemiercoles.api.access.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.Auditable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio Permission.
 * POJO puro sin anotaciones de frameworks.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Permission extends Auditable {

  private UUID id;
  private String name;
  private String description;

  public static Permission createNewPermission(String name, String description) {
    return Permission.builder()
        .name(name)
        .description(description)
        .statusRegistry("ACTIVE")
        .build();
  }

  public void updatePermission(String newName, String newDescription) {
    if (newName == null || newName.isBlank()) {
      throw new IllegalArgumentException("El nombre del permiso no puede estar vacío");
    }
    this.name = newName;
    if (newDescription != null) {
      this.description = newDescription;
    }
  }

  public void deletePermission() {
    if ("DELETE".equals(this.getStatusRegistry())) {
      return;
    }
    this.setStatusRegistry("DELETE");
  }

}
