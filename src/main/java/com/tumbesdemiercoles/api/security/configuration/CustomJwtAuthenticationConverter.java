package com.tumbesdemiercoles.api.security.configuration;

import com.tumbesdemiercoles.api.access.domain.event.UserPermissionsChangedEvent;
import com.tumbesdemiercoles.api.security.application.port.out.SecurityPermissionPort;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

  private final SecurityPermissionPort securityPermissionPort;

  private final Map<String, List<GrantedAuthority>> userPermissionsCache = new ConcurrentHashMap<>();

  @Override
  public Flux<GrantedAuthority> convert(Jwt jwt) {

    String userId = jwt.getSubject();
    List<String> roles = jwt.getClaimAsStringList("roles");

    Flux<GrantedAuthority> roleAuthorities = (roles == null || roles.isEmpty())
        ? Flux.empty()
        : Flux.fromIterable(roles).map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName));

    Flux<GrantedAuthority> permissionAuthorities;

    if (userPermissionsCache.containsKey(userId)) {
      log.info("[SecurityCache] HIT for user: {}", userId);
      permissionAuthorities = Flux.fromIterable(userPermissionsCache.get(userId));
    } else {
      log.info("[SecurityCache] MISS for user: {}. Fetching from database...", userId);
      permissionAuthorities = securityPermissionPort.getPermissionsForUser(UUID.fromString(userId))
          .map(permissionName -> (GrantedAuthority) new SimpleGrantedAuthority(permissionName.toUpperCase()))
          .collectList()
          .flatMapMany(authorities -> {
            log.info("[SecurityCache] Loading and Caching permissions for user {}: {}", userId, authorities);
            userPermissionsCache.put(userId, authorities);
            return Flux.fromIterable(authorities);
          });
    }

    return Flux.concat(roleAuthorities, permissionAuthorities).distinct();
  }

  public void evictCache(String userId) {
    log.info("[SecurityCache] Evicting cache for user: {}", userId);
    userPermissionsCache.remove(userId);
  }

  @EventListener
  public void onUserPermissionsChanged(UserPermissionsChangedEvent event) {
    log.info("[SecurityCache] Event received UserPermissionsChangedEvent for user: {}", event.userId());
    if (event.userId() != null) {
      evictCache(event.userId().toString());
    }
  }
}