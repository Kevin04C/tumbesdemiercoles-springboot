package com.tumbesdemiercoles.api.shared.infrastructure.database;

import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class R2dbcPaginationHelper {

  private final R2dbcEntityTemplate template;

  /**
   * @param criteria    Los filtros dinámicos que construiste.
   * @param pageable    El objeto de paginación de Spring.
   * @param entityClass La clase de la tabla (ej. UserEntity.class).
   * @param mapper      La función para convertir Entity a Dominio (ej. mapper::toDomain).
   * @param currentPage La página actual que solicitó el usuario.
   * @param <E>         El tipo del Entity (Infraestructura).
   * @param <D>         El tipo del Dominio.
   */
  public <E, D> Mono<PaginatedResult<D>> getPage(
      Criteria criteria,
      Pageable pageable,
      Class<E> entityClass,
      Function<E, D> mapper,
      int currentPage) {

    Query query = Query.query(criteria).with(pageable);
    Query countQuery = Query.query(criteria);

    return Mono.zip(
        template.select(entityClass).matching(query).all().collectList(),
        template.count(countQuery, entityClass)
    ).map(tuple -> {
      List<D> domainList = tuple.getT1().stream()
          .map(mapper)
          .toList();

      long totalElements = tuple.getT2();
      int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / pageable.getPageSize());

      return PaginatedResult.<D>builder()
          .content(domainList)
          .totalElements(totalElements)
          .currentPage(currentPage)
          .pageSize(pageable.getPageSize())
          .totalPages(totalPages)
          .build();
    });
  }

}
