package com.tumbesdemiercoles.api.access.domain.model;

import com.tumbesdemiercoles.api.shared.constants.StatusRegistryConst;
import com.tumbesdemiercoles.api.shared.domain.model.Auditable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio UserRole.
 * Representa la relación N:M entre User y Role.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRole extends Auditable {

  private UUID id;
  private UUID userId;
  private UUID roleId;

  public static UserRole assignRole(UUID userId, UUID roleId) {
    return UserRole.builder()
      .userId(userId)
      .roleId(roleId)
      .statusRegistry(StatusRegistryConst.ACTIVE)
      .build();
  }

  public void revokeRole() {
    if (StatusRegistryConst.DELETE.equals(this.getStatusRegistry())) {
      return;
    }
    this.setStatusRegistry(StatusRegistryConst.DELETE);
  }

}
