package com.tumbesdemiercoles.api.news.presentation.dto.request;

import com.tumbesdemiercoles.api.shared.presentation.dto.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewsByCategorySlugFilterRequest extends BasePageRequest {

}
