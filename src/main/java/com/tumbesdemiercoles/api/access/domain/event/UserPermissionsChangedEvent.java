package com.tumbesdemiercoles.api.access.domain.event;

import java.util.UUID;

public record UserPermissionsChangedEvent(UUID userId) {
}
