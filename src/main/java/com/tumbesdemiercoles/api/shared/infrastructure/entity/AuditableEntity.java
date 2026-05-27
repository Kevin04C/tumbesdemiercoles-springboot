package com.tumbesdemiercoles.api.shared.infrastructure.entity;

import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Clase base abstracta que centraliza los campos de auditoría y estado
 * comunes a todas las entidades del sistema.
 *
 * <p>Esta clase existe para evitar la repetición de columnas en cada tabla.
 * Las entidades concretas deben extender de {@code AuditableEntity} para
 * heredar automáticamente estos atributos.</p>
 *
 * <p>Spring Data (JDBC/R2DBC) mapeará estos campos heredados como columnas
 * normales en la tabla de cada entidad hija.</p>
 *
 * <p><b>Nota:</b> Esta clase es abstracta porque no representa una tabla por sí misma;
 * únicamente define atributos compartidos.</p>
 *
 * <p>Lombok:</p>
 * <ul>
 *   <li>{@link Data} genera getters, setters, {@code toString()}, {@code equals()}
 *       y {@code hashCode()}.</li>
 *   <li>{@link NoArgsConstructor} genera un constructor vacío requerido
 *       por frameworks de persistencia.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditableEntity {

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;

  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;

  @Column("status_registry")
  private String statusRegistry;

  @Column("status_updated_at")
  private OffsetDateTime statusUpdatedAt;
}
