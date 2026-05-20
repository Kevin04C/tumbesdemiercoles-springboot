package com.tumbesdemiercoles.api.columnist.domain.model;

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
public class ColumnistFilter extends BasePaginated {
    private UUID id;
    private String author;
    private String title;
}
