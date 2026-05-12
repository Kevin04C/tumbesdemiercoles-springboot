package com.tumbesdemiercoles.api.shared.infrastructure.database;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaHelper {

  public static Criteria addLike(Criteria current, String column, String value) {
    if (value != null && !value.isBlank()) {
      String term = "%" + value.trim() + "%";
      return current.and(Criteria.where(column).like(term).ignoreCase(true));
    }
    return current;
  }

  /**
   * Agrega una condición IS (igualdad exacta) si el valor no es nulo.
   * Sirve para UUIDs, Booleans, Integers, etc.
   */
  public static Criteria addEquals(Criteria current, String column, Object value) {
    if (value != null) {
      if (value instanceof String str && str.isBlank()) {
        return current;
      }
      return current.and(Criteria.where(column).is(value));
    }
    return current;
  }

  /**
   * Búsqueda en una lista de opciones (IN).
   * Ej: roles en ["ADMIN", "USER"]
   */
  public static Criteria addIn(Criteria current, String column, Collection<?> values) {
    if (values != null && !values.isEmpty()) {
      return current.and(Criteria.where(column).in(values));
    }
    return current;
  }

  /**
   * Mayor o igual que (>=). Útil para fechas (startDate) o precios mínimos.
   */
  public static Criteria addGreaterThanOrEquals(Criteria current, String column, Object value) {
    if (value != null) {
      return current.and(Criteria.where(column).greaterThanOrEquals(value));
    }
    return current;
  }

  /**
   * Menor o igual que (<=). Útil para fechas (endDate) o precios máximos.
   */
  public static Criteria addLessThanOrEquals(Criteria current, String column, Object value) {
    if (value != null) {
      return current.and(Criteria.where(column).lessThanOrEquals(value));
    }
    return current;
  }
}
