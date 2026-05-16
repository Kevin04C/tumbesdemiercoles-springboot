package com.tumbesdemiercoles.api.access.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.Auditable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio RolePermission.
 * Representa la relación N:M entre Role y Permission.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RolePermission extends Auditable {

  private UUID id;
  private UUID roleId;
  private UUID permissionId;

  public static RolePermission assignPermission(UUID roleId, UUID permissionId) {
    return RolePermission.builder()
        .roleId(roleId)
        .permissionId(permissionId)
        .statusRegistry("ACTIVE")
        .build();
  }

  public void revokePermission() {
    if ("DELETE".equals(this.getStatusRegistry())) {
      return;
    }
    this.setStatusRegistry("DELETE");
  }

}
