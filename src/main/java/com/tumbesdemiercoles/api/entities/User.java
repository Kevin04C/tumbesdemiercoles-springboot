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
@Table("users")
public class User extends AuditableEntity{

  @Id
  @Column("UserID")
  private UUID userId;

  @Column("FirstName")
  private String firstName;

  @Column("LastName")
  private String lastName;

  @Column("DocumentTypeID")
  private Long documentTypeId;

  @Column("DocumentNumber")
  private String documentNumber;

  @Column("UserEmail")
  private String userEmail;

  @Column("UserImageUrl")
  private String userImageUrl;

  @Column("EmailVerified")
  private Boolean emailVerified;

  @Column("PasswordHash")
  private String passwordHash;

}