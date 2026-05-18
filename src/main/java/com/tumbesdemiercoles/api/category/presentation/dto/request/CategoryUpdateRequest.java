package com.tumbesdemiercoles.api.category.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {
    private String descripcion;
    private boolean isActive;
    private UUID categoryId;
}
