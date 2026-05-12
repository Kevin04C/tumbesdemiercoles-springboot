package com.tumbesdemiercoles.api.user.infrastructure.entity;

import com.tumbesdemiercoles.api.shared.infrastructure.entity.AuditableEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@Table("user")
public class UserEntity extends AuditableEntity {

  @Id
  @Column("id")
  private UUID id;

  @Column("first_name")
  private String firstName;

  @Column("last_name")
  private String lastName;

  @Column("email")
  private String userEmail;

  @Column("image_url")
  private String userImageUrl;

  @Column("is_email_verified")
  private Boolean isEmailVerified;

  @Column("password_hash")
  private String passwordHash;

  @Column("is_active")
  private Boolean isActive;

}
