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
@Table("user_role")
public class UserRoleEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("user_id")
  private UUID userId;

  @Column("role_id")
  private UUID roleId;

}
