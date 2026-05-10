package com.tumbesdemiercoles.api.user.domain.event;

import com.tumbesdemiercoles.api.user.domain.model.User;

public record UserRegisteredEvent(User user) {
}
