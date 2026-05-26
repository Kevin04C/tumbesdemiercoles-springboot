package com.tumbesdemiercoles.api.digitalweekly.infrastructure.entity;

import com.tumbesdemiercoles.api.shared.infrastructure.entity.AuditableEntity;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entidad de INFRAESTRUCTURA: aquí SÍ van las anotaciones de Spring Data R2DBC.
 * Solo existe para hablar con la base de datos.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Table("digital_weekly")
public class DigitalWeeklyEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("pdf_url")
  private String pdfUrl;

  @Column("front_page_image_url")
  private String frontPageImageUrl;

  @Column("descripcion")
  private String descripcion;

  @Column("is_active")
  private Boolean isActive;

  @Column("is_premium")
  private Boolean isPremium;

  @Column("url")
  private String url;
}
