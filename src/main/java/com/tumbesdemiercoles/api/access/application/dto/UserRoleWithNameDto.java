package com.tumbesdemiercoles.api.access.application.dto;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRoleWithNameDto(
    UUID id,
    @Column("user_id") UUID userId,
    @Column("role_id") UUID roleId,
    @Column("role_name") String roleName,
    @Column("status_registry") String statusRegistry,
    @Column("created_at") LocalDateTime createdAt,
    @Column("updated_at") LocalDateTime updatedAt
) {}
