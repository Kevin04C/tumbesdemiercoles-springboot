package com.tumbesdemiercoles.api.access.infrastructure.entity;

import com.tumbesdemiercoles.api.shared.infrastructure.entity.AuditableEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("permission")
public class PermissionEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("name")
  private String permissionName;

  @Column("description")
  private String permissionDescription;

}
