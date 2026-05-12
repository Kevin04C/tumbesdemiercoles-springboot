package com.tumbesdemiercoles.api.user.application.ports.out;

import com.tumbesdemiercoles.api.user.domain.model.User;
import reactor.core.publisher.Mono;

public interface TokenProviderPort {

    Mono<String> generateToken(User user);

}
