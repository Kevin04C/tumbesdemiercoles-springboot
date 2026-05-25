package com.tumbesdemiercoles.api.access.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignUserRoleRequest(
    @NotNull(message = "Role ID must not be null")
    UUID roleId
) {}
