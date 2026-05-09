package com.tumbesdemiercoles.api.shared.infrastructure.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "statusRegistry", ignore = true)
@Mapping(target = "statusUpdatedAt", ignore = true)
public @interface IgnoreAuditingFields {
}
