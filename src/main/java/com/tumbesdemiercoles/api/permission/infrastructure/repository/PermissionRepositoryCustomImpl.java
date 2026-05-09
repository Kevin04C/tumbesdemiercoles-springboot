package com.tumbesdemiercoles.api.permission.infrastructure.repository;

import com.tumbesdemiercoles.api.permission.infrastructure.entity.PermissionEntity;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.dto.permissionFilter.PermissionFilter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryCustomImpl implements PermissionRepositoryCustom {

  private final R2dbcEntityTemplate template;
  private final DatabaseClient databaseClient;

  @Override
  public Mono<PageResponseDto<PermissionEntity>> findAllPermissionsPaginated(Pageable pageable, PermissionFilter filter) {

    Criteria criteria = Criteria.empty();

    if (filter.getName() != null && !filter.getName().isBlank()) {
      String term = "%" + filter.getName().trim() + "%";
      criteria = criteria.and(Criteria.where("PermissionName").like(term));
    }

    Query query = Query.query(criteria).with(pageable);
    Query countQuery = Query.query(criteria);

    return Mono.zip(
        template.select(PermissionEntity.class).matching(query).all().collectList(),
        template.count(countQuery, PermissionEntity.class)
    ).map(tuple -> PageResponseDto.<PermissionEntity>builder()
        .content(tuple.getT1())
        .totalElements(tuple.getT2())
        .page(pageable.getPageNumber() + 1)
        .size(pageable.getPageSize())
        .totalPages((long) Math.ceil((double) tuple.getT2() / pageable.getPageSize()))
        .build());
  }

  @Override
  public Mono<PageResponseDto<PermissionEntity>> findPermissionsByRoleIdPaginated(Long roleId, Pageable pageable, String keyword) {

    String baseCondition = "rp.RoleID = :roleId AND rp.StatusRegistry = 'ACTIVO' AND p.StatusRegistry = 'ACTIVO'";
    boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

    String sqlWhere = baseCondition;
    if (hasKeyword) {
      sqlWhere += " AND (p.PermissionName LIKE :keyword OR p.Description LIKE :keyword)";
    }

    String countSql = "SELECT COUNT(p.PermissionID) FROM dbo.Permissions p " +
        "JOIN dbo.RolePermissions rp ON p.PermissionID = rp.PermissionID WHERE " + sqlWhere;

    String sortProperty = pageable.getSort().iterator().next().getProperty();
    String sortDirection = pageable.getSort().iterator().next().isAscending() ? "ASC" : "DESC";

    String dataSql = "SELECT p.PermissionID, p.PermissionName, p.Description FROM dbo.Permissions p " +
        "JOIN dbo.RolePermissions rp ON p.PermissionID = rp.PermissionID WHERE " + sqlWhere +
        " ORDER BY p." + sortProperty + " " + sortDirection +
        " OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY";

    var countSpec = databaseClient.sql(countSql).bind("roleId", roleId);
    if (hasKeyword) countSpec = countSpec.bind("keyword", "%" + keyword.trim() + "%");
    Mono<Long> totalMono = countSpec.map(row -> row.get(0, Long.class)).first().defaultIfEmpty(0L);

    var dataSpec = databaseClient.sql(dataSql)
        .bind("roleId", roleId)
        .bind("offset", pageable.getOffset())
        .bind("size", pageable.getPageSize());
    if (hasKeyword) dataSpec = dataSpec.bind("keyword", "%" + keyword.trim() + "%");

    Mono<List<PermissionEntity>> dataMono = dataSpec.map((row, meta) -> {
      PermissionEntity p = new PermissionEntity();
      p.setId(row.get("PermissionID", UUID.class));
      p.setPermissionName(row.get("PermissionName", String.class));
      p.setDescription(row.get("Description", String.class));
      return p;
    }).all().collectList();

    return Mono.zip(totalMono, dataMono)
        .map(tuple -> {
          Long total = tuple.getT1();
          List<PermissionEntity> list = tuple.getT2();
          int totalPages = total == 0 ? 0 : (int) Math.ceil((double) total / pageable.getPageSize());

          return PageResponseDto.<PermissionEntity>builder()
              .content(list)
              .page(pageable.getPageNumber() + 1)
              .size(pageable.getPageSize())
              .totalElements(total)
              .totalPages(totalPages)
              .build();
        });
  }
}
