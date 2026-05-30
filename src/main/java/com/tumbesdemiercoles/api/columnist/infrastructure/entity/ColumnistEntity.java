package com.tumbesdemiercoles.api.columnist.infrastructure.entity;

import com.tumbesdemiercoles.api.shared.infrastructure.entity.AuditableEntity;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@Table("columnist")
public class ColumnistEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("content")
  private String content;

  @Column("author")
  private String author;

  @Column("title")
  private String title;

  @Column("slug")
  private String slug;

  @Column("headline")
  private String headline;

  @Column("author_image_url")
  private String authorImageUrl;

  @Column("is_active")
  private Boolean isActive;
}
