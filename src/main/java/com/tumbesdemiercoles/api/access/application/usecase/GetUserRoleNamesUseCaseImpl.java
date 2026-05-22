package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.GetUserRoleNamesUseCase;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserRoleNamesUseCaseImpl implements GetUserRoleNamesUseCase {

    private final UserRoleRepository userRoleRepository;

    @Override
    public Flux<String> execute(UUID userId) {
        return userRoleRepository.findRoleNamesByUserId(userId);
    }
}
