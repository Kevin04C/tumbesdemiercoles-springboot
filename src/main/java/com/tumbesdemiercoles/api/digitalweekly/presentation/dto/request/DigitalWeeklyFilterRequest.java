package com.tumbesdemiercoles.api.digitalweekly.presentation.dto.request;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
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
public class DigitalWeeklyFilterRequest extends BasePageRequest {
  private String descripcion;
  private Boolean isActive;
  private Boolean isPremium;
}
