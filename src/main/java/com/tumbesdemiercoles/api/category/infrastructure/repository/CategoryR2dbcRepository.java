package com.tumbesdemiercoles.api.category.infrastructure.repository;

import com.tumbesdemiercoles.api.category.infrastructure.entity.CategoryEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repositorio de Spring Data R2DBC para la entidad CategoryEntity.
 * Solo existe en la capa de infraestructura.
 */
public interface CategoryR2dbcRepository extends ReactiveCrudRepository<CategoryEntity, UUID> {

}
