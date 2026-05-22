package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.FindUserRoleUseCase;
import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FindUserRoleUseCaseImpl implements FindUserRoleUseCase {

  private final UserRoleRepository userRoleRepository;

  public Flux<UserRole> findByUserId(UUID userId) {
    return userRoleRepository.findByUserId(userId);
  }
}
