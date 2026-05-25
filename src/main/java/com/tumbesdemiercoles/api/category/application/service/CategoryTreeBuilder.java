package com.tumbesdemiercoles.api.category.application.service;

import com.tumbesdemiercoles.api.category.application.dto.CategoryResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CategoryTreeBuilder {

  public List<CategoryResponseDto> buildTree(List<CategoryResponseDto> flatList) {
    Map<UUID, CategoryResponseDto> nodeMap = indexById(flatList);
    return buildHierarchy(flatList, nodeMap);
  }

  private Map<UUID, CategoryResponseDto> indexById(List<CategoryResponseDto> flatList) {
    Map<UUID, CategoryResponseDto> nodeMap = new HashMap<>();
    for (CategoryResponseDto dto : flatList) {
      nodeMap.put(dto.getId(), dto);
    }
    return nodeMap;
  }

  private List<CategoryResponseDto> buildHierarchy(List<CategoryResponseDto> flatList, Map<UUID, CategoryResponseDto> nodeMap) {
    List<CategoryResponseDto> roots = new ArrayList<>();
    for (CategoryResponseDto dto : flatList) {
      if (dto.getCategoryId() != null && nodeMap.containsKey(dto.getCategoryId())) {
        CategoryResponseDto parent = nodeMap.get(dto.getCategoryId());
        if (parent.getChildren() == null) {
          parent.setChildren(new ArrayList<>());
        }
        parent.getChildren().add(dto);
      } else {
        roots.add(dto);
      }
    }
    return roots;
  }
}
