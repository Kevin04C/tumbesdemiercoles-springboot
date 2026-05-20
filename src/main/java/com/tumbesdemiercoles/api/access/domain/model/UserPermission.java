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
 * Entidad de dominio UserPermission.
 * Representa la relación N:M entre User y Permission con campo is_active.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPermission extends Auditable {

  private UUID id;
  private UUID userId;
  private UUID permissionId;
  private Boolean isActive;

  public static UserPermission assignPermission(UUID userId, UUID permissionId) {
    return UserPermission.builder()
        .userId(userId)
        .permissionId(permissionId)
        .isActive(true)
        .statusRegistry(StatusRegistryConst.ACTIVE)
        .build();
  }

  public void revokePermission() {
    if (StatusRegistryConst.DELETE.equals(this.getStatusRegistry())) {
      return;
    }
    this.isActive = false;
    this.setStatusRegistry(StatusRegistryConst.DELETE);
  }

}
