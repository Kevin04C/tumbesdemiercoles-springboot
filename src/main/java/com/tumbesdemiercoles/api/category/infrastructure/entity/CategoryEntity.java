package com.tumbesdemiercoles.api.category.infrastructure.entity;

import com.tumbesdemiercoles.api.user.infrastructure.entity.AuditableEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entidad de INFRAESTRUCTURA: aquí SÍ van las anotaciones de Spring Data R2DBC.
 * Solo existe para hablar con la base de datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("category")
public class CategoryEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("description")
  private String description;

  @Column("is_active")
  private Boolean isActive;

}
