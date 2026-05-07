package com.tumbesdemiercoles.api.entities;

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
public class Permission extends AuditableEntity {

  @Id
  @Column("PermissionID")
  private UUID permissionId;

  @Column("PermissionName")
  private String permissionName;

  @Column("Description")
  private String description;

}