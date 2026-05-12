package com.tumbesdemiercoles.api.user.infrastructure.adapter;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.infrastructure.database.CriteriaHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.database.R2dbcPaginationHelper;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import com.tumbesdemiercoles.api.user.infrastructure.entity.UserEntity;
import com.tumbesdemiercoles.api.user.infrastructure.mapper.UserPersistenceMapper;
import com.tumbesdemiercoles.api.user.infrastructure.repository.UserR2dbcRepository;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserFilterRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador: implementa la interfaz UserRepository del dominio
 * usando Spring Data R2DBC por debajo.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final UserR2dbcRepository r2dbcRepository;
  private final R2dbcPaginationHelper paginationHelper;
  private final UserPersistenceMapper mapper;

  @Override
  public Mono<User> save(User user) {
    return r2dbcRepository.save(user.getId() == null ? mapper.toEntity(user) : mapper.toEntityUpdate(user))
        .map(mapper::toDomain);
  }

  @Override
  public Mono<User> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<User> findByEmail(String email) {
    return r2dbcRepository.findByUserEmail(email)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<User> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Boolean> existsByEmail(String email) {
    return r2dbcRepository.existsByUserEmail(email);
  }

  @Override
  public Mono<PageResponseDto<User>> findUsers(UserFilterRequest filter) {

    Criteria criteria = Criteria.empty();
    criteria = CriteriaHelper.addEquals(criteria, "id", filter.getUserId());
    criteria = CriteriaHelper.addLike(criteria, "first_name", filter.getFirstName());
    criteria = CriteriaHelper.addLike(criteria, "last_name", filter.getLastName());
    criteria = CriteriaHelper.addLike(criteria, "email", filter.getEmail());
    return paginationHelper.getPage(
        criteria,
        filter.toPageable(),
        UserEntity.class,
        mapper::toDomain,
        filter.getPage()
    );
  }

}
