package com.tumbesdemiercoles.api.permission.infrastructure.entity;

import com.tumbesdemiercoles.api.user.infrastructure.entity.AuditableEntity;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table("Permissions")
public class PermissionEntity extends AuditableEntity {

  @Id
  @Column("PermissionID")
  private UUID id;

  @Column("PermissionName")
  private String permissionName;

  @Column("Description")
  private String description;

}
