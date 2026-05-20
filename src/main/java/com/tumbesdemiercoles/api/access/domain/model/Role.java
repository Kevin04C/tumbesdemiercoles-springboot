package com.tumbesdemiercoles.api.access.domain.model;

import com.tumbesdemiercoles.api.shared.constants.shared.StatusRegistryConst;
import com.tumbesdemiercoles.api.shared.domain.model.Auditable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio Role.
 * POJO puro sin anotaciones de frameworks.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends Auditable {

  private UUID id;
  private String name;

  public static Role createNewRole(String name) {
    return Role.builder()
        .name(name)
        .statusRegistry(StatusRegistryConst.ACTIVE)
        .build();
  }

  public void updateName(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
    }
    this.name = newName;
  }

  public void deleteRole() {
    if (StatusRegistryConst.DELETE.equals(this.getStatusRegistry())) {
      return;
    }
    this.setStatusRegistry(StatusRegistryConst.DELETE);
  }

}
