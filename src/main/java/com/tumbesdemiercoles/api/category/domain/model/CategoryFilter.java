package com.tumbesdemiercoles.api.category.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.BasePaginated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryFilter extends BasePaginated {
    private UUID id;
    private String description;
}
