package com.tumbesdemiercoles.api.category.presentation.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class CategoryResponse {
  private UUID id;
  private String description;
  private Boolean isActive;
  private UUID categoryId;
  private String slug;
  private List<CategoryResponse> children;
}
