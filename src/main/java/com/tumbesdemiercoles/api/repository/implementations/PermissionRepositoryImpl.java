package com.tumbesdemiercoles.api.repository.implementations;

import com.tumbesdemiercoles.api.entities.Permission;
import com.tumbesdemiercoles.api.entities.dtos.PageResponseDto;
import com.tumbesdemiercoles.api.entities.dtos.permissionFilter.PermissionFilter;
import com.tumbesdemiercoles.api.repository.custom.PermissionRepositoryCustom;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepositoryCustom {

  private final R2dbcEntityTemplate template;
  private final DatabaseClient databaseClient;

  @Override
  public Mono<PageResponseDto<Permission>> findAllPermissionsPaginated(Pageable pageable, PermissionFilter filter) {

    Criteria criteria = Criteria.empty();

    if (filter.getName() != null && !filter.getName().isBlank()) {
      String term = "%" + filter.getName().trim() + "%";
      criteria = criteria.and(Criteria.where("PermissionName").like(term));
    }

    Query query = Query.query(criteria).with(pageable);
    Query countQuery = Query.query(criteria);

    return Mono.zip(
        template.select(Permission.class).matching(query).all().collectList(),
        template.count(countQuery, Permission.class)
    ).map(tuple -> PageResponseDto.<Permission>builder()
        .content(tuple.getT1())
        .totalElements(tuple.getT2())
        .page(pageable.getPageNumber() + 1)
        .size(pageable.getPageSize())
        .totalPages((long) Math.ceil((double) tuple.getT2() / pageable.getPageSize()))
        .build());
  }

  @Override
  public Mono<PageResponseDto<Permission>> findPermissionsByRoleIdPaginated(Long roleId, Pageable pageable, String keyword) {

    String baseCondition = "rp.RoleID = :roleId AND rp.StatusRegistry = 'ACTIVO' AND p.StatusRegistry = 'ACTIVO'";
    boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

    String sqlWhere = baseCondition;
    if (hasKeyword) {
      sqlWhere += " AND (p.PermissionName LIKE :keyword OR p.Description LIKE :keyword)";
    }

    String countSql = "SELECT COUNT(p.PermissionID) FROM dbo.Permissions p " +
        "JOIN dbo.RolePermissions rp ON p.PermissionID = rp.PermissionID WHERE " + sqlWhere;

    // Extraemos la dirección de ordenamiento desde tu Pageable dinámicamente
    String sortProperty = pageable.getSort().iterator().next().getProperty();
    String sortDirection = pageable.getSort().iterator().next().isAscending() ? "ASC" : "DESC";

    String dataSql = "SELECT p.PermissionID, p.PermissionName, p.Description FROM dbo.Permissions p " +
        "JOIN dbo.RolePermissions rp ON p.PermissionID = rp.PermissionID WHERE " + sqlWhere +
        " ORDER BY p." + sortProperty + " " + sortDirection +
        " OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY";

    // Ejecutamos el COUNT
    var countSpec = databaseClient.sql(countSql).bind("roleId", roleId);
    if (hasKeyword) countSpec = countSpec.bind("keyword", "%" + keyword.trim() + "%");
    Mono<Long> totalMono = countSpec.map(row -> row.get(0, Long.class)).first().defaultIfEmpty(0L);

    // Ejecutamos la DATA usando tu Pageable
    var dataSpec = databaseClient.sql(dataSql)
        .bind("roleId", roleId)
        // Aquí usamos la magia de tu toPageable() que ya calculó el offset
        .bind("offset", pageable.getOffset())
        .bind("size", pageable.getPageSize());
    if (hasKeyword) dataSpec = dataSpec.bind("keyword", "%" + keyword.trim() + "%");

    Mono<List<Permission>> dataMono = dataSpec.map((row, meta) -> {
      Permission p = new Permission();
      p.setPermissionId(row.get("PermissionID", UUID.class));
      p.setPermissionName(row.get("PermissionName", String.class));
      p.setDescription(row.get("Description", String.class));
      return p;
    }).all().collectList();

    return Mono.zip(totalMono, dataMono)
        .map(tuple -> {
          Long total = tuple.getT1();
          List<Permission> list = tuple.getT2();
          int totalPages = total == 0 ? 0 : (int) Math.ceil((double) total / pageable.getPageSize());

          return PageResponseDto.<Permission>builder()
              .content(list)
              // Le sumamos 1 porque Spring Pageable usa base 0, pero tu frontend usa base 1
              .page(pageable.getPageNumber() + 1)
              .size(pageable.getPageSize())
              .totalElements(total)
              .totalPages(totalPages)
              .build();
        });
  }
}