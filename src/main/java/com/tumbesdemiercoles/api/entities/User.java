package com.tumbesdemiercoles.api.entities;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("account")
public class User extends AuditableEntity{

  @Id
  @Column("user_id")
  private UUID userId;

  @Column("first_name")
  private String firstName;

  @Column("last_name")
  private String lastName;

  @Column("email")
  private String userEmail;

  @Column("image_url")
  private String userImageUrl;

  @Column("is_email_verified")
  private Boolean emailVerified;

  @Column("password_hash")
  private String passwordHash;

}