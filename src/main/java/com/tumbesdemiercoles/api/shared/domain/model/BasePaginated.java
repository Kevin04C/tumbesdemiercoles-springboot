package com.tumbesdemiercoles.api.shared.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasePaginated {

  private int page;
  private int size;
  private String sortBy;
  private String sortDirection;

}
