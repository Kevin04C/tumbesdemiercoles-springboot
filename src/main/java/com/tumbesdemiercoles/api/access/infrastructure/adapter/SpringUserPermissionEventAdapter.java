package com.tumbesdemiercoles.api.access.infrastructure.adapter;

import com.tumbesdemiercoles.api.access.application.ports.out.UserPermissionEventPublisherPort;
import com.tumbesdemiercoles.api.access.domain.event.UserPermissionsChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SpringUserPermissionEventAdapter implements UserPermissionEventPublisherPort {

    private final ApplicationEventPublisher springPublisher;

    @Override
    public Mono<Void> publishPermissionsChanged(UUID userId) {
        return Mono.fromRunnable(() ->
                        springPublisher.publishEvent(new UserPermissionsChangedEvent(userId)))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}