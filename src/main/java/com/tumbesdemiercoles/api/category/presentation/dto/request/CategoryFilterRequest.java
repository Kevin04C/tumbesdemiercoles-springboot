package com.tumbesdemiercoles.api.category.presentation.dto.request;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
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
public class CategoryFilterRequest extends BasePageRequest {
    private UUID id;
    private String description;
}
