package com.tumbesdemiercoles.api.columnist.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnistUpdateRequest {
    private String content;
    private String author;
    private String title;
    private String headline;
    private String authorImageUrl;
    private Boolean isActive;
}
