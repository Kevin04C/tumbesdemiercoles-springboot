package com.tumbesdemiercoles.api.shared.infrastructure.mapper;

import com.tumbesdemiercoles.api.shared.domain.model.BasePaginated;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableMapper {

  public static Pageable toPageable(BasePaginated basePaginated) {

    Sort.Direction direction = basePaginated.getSortDirection().equalsIgnoreCase("desc")
        ? Sort.Direction.DESC : Sort.Direction.ASC;

    return PageRequest.of(
        basePaginated.getPage(),
        basePaginated.getSize(),
        Sort.by(direction, basePaginated.getSortBy())
    );
  }
}
