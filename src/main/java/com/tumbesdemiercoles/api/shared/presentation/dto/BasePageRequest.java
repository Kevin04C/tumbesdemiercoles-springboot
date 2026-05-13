package com.tumbesdemiercoles.api.shared.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasePageRequest {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "id";

    @Builder.Default
    private String sortDir = "asc";

    public int getOffsetPage() {
        return Math.max(0, this.page - 1);
    }

//    public Pageable toPageable() {
//        Sort.Direction direction = this.getSortDir().equalsIgnoreCase("desc")
//                ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        return PageRequest.of(
//                this.getOffsetPage(),
//                this.size,
//                Sort.by(direction, this.getSortBy())
//        );
//    }
}
