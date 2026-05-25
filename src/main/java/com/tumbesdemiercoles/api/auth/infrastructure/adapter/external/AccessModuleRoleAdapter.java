package com.tumbesdemiercoles.api.auth.infrastructure.adapter.external;

import com.tumbesdemiercoles.api.access.application.ports.in.GetUserRoleNamesUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.out.FetchUserRolesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccessModuleRoleAdapter implements FetchUserRolesPort {

    private final GetUserRoleNamesUseCase getUserRoleNamesUseCase;

    @Override
    public Mono<List<String>> getRolesForUser(UUID userId) {
        return getUserRoleNamesUseCase.execute(userId)
                .collectList();
    }
}