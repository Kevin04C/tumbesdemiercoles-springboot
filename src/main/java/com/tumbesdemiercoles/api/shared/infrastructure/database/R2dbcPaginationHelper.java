package com.tumbesdemiercoles.api.shared.infrastructure.database;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
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
  public <E, D> Mono<PageResponseDto<D>> getPage(
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
      long totalPages = totalElements == 0 ? 0 : (long) Math.ceil((double) totalElements / pageable.getPageSize());

      return PageResponseDto.<D>builder()
          .content(domainList)
          .totalElements(totalElements)
          .page(currentPage)
          .size(pageable.getPageSize())
          .totalPages(totalPages)
          .build();
    });
  }

}
