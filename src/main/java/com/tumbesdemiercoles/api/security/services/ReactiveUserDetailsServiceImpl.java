package com.tumbesdemiercoles.api.security.services;

import com.tumbesdemiercoles.api.repository.UserRepository;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;


/**
 * Implementación reactiva de {@link ReactiveUserDetailsService} que carga
 * los datos de usuario desde la base de datos y construye un
 * {@link UserPrincipal} con sus roles.
 */
@Slf4j
@AllArgsConstructor
@Service
@Primary
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

  private final UserRepository userRepository;

  @Override
  public Mono<UserDetails> findByUsername(String username) {

    return userRepository.findByUserEmail(username)
        .flatMap(user ->
            userRepository.findAuthoritiesByUserId(user.getUserId())
                .collectList()
                .map(authorityDtos -> {

                  Set<GrantedAuthority> authorities = new HashSet<>();
                  Set<String> roles = new HashSet<>();


                  for (var dto : authorityDtos) {
                    if (dto.roleName() != null) {
                      String roleName = dto.roleName().toUpperCase();

                      roles.add(roleName);

                      authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                    }

                    if (dto.permissionName() != null) {
                      authorities.add(new SimpleGrantedAuthority(dto.permissionName().toUpperCase()));
                    }
                  }

                  return new UserPrincipal(
                      String.valueOf(user.getUserId()),
                      user.getUserEmail(),
                      user.getPasswordHash(),
                      roles,
                      authorities
                  );
                })
        );
  }
}