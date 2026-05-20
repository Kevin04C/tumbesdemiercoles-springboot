package com.tumbesdemiercoles.api.digitalweekly.infrastructure.repository;

import com.tumbesdemiercoles.api.digitalweekly.infrastructure.entity.DigitalWeeklyEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repositorio de Spring Data R2DBC para la entidad DigitalWeeklyEntity.
 * Solo existe en la capa de infraestructura.
 */
public interface DigitalWeeklyR2dbcRepository extends ReactiveCrudRepository<DigitalWeeklyEntity, UUID> {

}
