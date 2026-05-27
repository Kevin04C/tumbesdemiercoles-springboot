package com.tumbesdemiercoles.api.access.presentation.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserRoleResponse(
    UUID id,
    UUID userId,
    UUID roleId,
    String roleName,
    String statusRegistry,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
