package com.tumbesdemiercoles.api.auth.infrastructure.adapter;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.auth.application.ports.out.FetchUserRolesPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenProviderPort {

    private final JwtUtil jwtUtil;
    private final FetchUserRolesPort fetchUserRolesPort;

    @Override
    public Mono<String> generateToken(AuthUserResponseDto userDto) {
        return fetchUserRolesPort.getRolesForUser(userDto.getId())
                .map(rolesList -> {
                    UserPrincipal principal = UserPrincipal.builder()
                            .id(userDto.getId().toString())
                            .roles(new HashSet<>(rolesList))
                            .build();

                    return jwtUtil.generateToken(principal);
                });
    }

    @Override
    public Mono<String> generateToken(AuthUserDetailsDto userDetailsDto) {
        return fetchUserRolesPort.getRolesForUser(userDetailsDto.getId())
                .map(rolesList -> {
                    UserPrincipal principal = UserPrincipal.builder()
                            .id(userDetailsDto.getId().toString())
                            .roles(new HashSet<>(rolesList))
                            .build();

                    return jwtUtil.generateToken(principal);
                });
    }
}
