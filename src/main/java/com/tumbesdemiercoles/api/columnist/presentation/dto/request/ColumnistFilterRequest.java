package com.tumbesdemiercoles.api.columnist.presentation.dto.request;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
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
public class ColumnistFilterRequest extends BasePageRequest {
    private UUID id;
    private String author;
    private String title;
    private String slug;
}
