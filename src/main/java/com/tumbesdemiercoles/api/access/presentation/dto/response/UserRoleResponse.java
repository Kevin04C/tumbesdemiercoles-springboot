package com.tumbesdemiercoles.api.access.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRoleResponse(
    UUID id,
    UUID userId,
    UUID roleId,
    String roleName,
    String statusRegistry,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
