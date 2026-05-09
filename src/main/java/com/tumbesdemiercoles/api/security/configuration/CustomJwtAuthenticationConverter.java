package com.tumbesdemiercoles.api.security.configuration;

import com.tumbesdemiercoles.api.permission.infrastructure.repository.PermissionR2dbcRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

  private final PermissionR2dbcRepository permissionRepository;

  private final Map<String, List<GrantedAuthority>> permissionsCache = new ConcurrentHashMap<>();

  @Override
  public Flux<GrantedAuthority> convert(Jwt jwt) {

    List<String> roles = jwt.getClaimAsStringList("roles");

    if (roles == null || roles.isEmpty()) {
      return Flux.empty();
    }

    Flux<GrantedAuthority> roleAuthorities = Flux.fromIterable(roles)
        .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName));

    Flux<GrantedAuthority> permissionAuthorities = Flux.fromIterable(roles)
        .flatMap(roleName -> {

          if (permissionsCache.containsKey(roleName)) {
            return Flux.fromIterable(permissionsCache.get(roleName));
          }

          return permissionRepository.findPermissionNamesByRoleName(roleName)
              .map(permissionName -> (GrantedAuthority) new SimpleGrantedAuthority(permissionName))
              .collectList()
              .flatMapMany(authorities -> {
                permissionsCache.put(roleName, authorities);
                return Flux.fromIterable(authorities);
              });
        });

    return Flux.concat(roleAuthorities, permissionAuthorities).distinct();
  }

  public void evictCache(String roleName) {
    permissionsCache.remove(roleName);
  }
}