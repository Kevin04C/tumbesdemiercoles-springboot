package com.tumbesdemiercoles.api.user.infrastructure.adapter;

import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import com.tumbesdemiercoles.api.user.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenProviderPort {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<String> generateToken(User user) {
        return Mono.fromCallable(() -> {

            // 1. Traducimos tu User de Dominio al UserPrincipal que pide tu JwtUtil
            UserPrincipal principal = UserPrincipal.builder()
                    .id(user.getId().toString())
                    .email(user.getEmail())
                    .build();

            // 2. Usamos tu excelente utilidad para generar el token
            return jwtUtil.generateToken(principal);
        });
    }
}
