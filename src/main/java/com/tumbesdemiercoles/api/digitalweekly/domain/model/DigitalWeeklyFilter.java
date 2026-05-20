package com.tumbesdemiercoles.api.digitalweekly.domain.model;

import com.tumbesdemiercoles.api.shared.domain.model.BasePaginated;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DigitalWeeklyFilter extends BasePaginated {
  private UUID id;
  private String descripcion;
  private Boolean isActive;
  private Boolean isPremium;
}
