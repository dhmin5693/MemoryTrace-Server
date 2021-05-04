package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "int", value = "조회할 페이지")
    private int page;

    @ApiModelProperty(position = 2, required = true, dataType = "int", value = "페이지 크기")
    private int size;

    public Pageable getPageableWithSort(PageRequestDto pageRequestDto) {
        return PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(),
            Sort.by("modifiedDate").descending());
    }

    public Pageable getPageableWithBookSort(PageRequestDto pageRequestDto) {
        return PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(),
            Sort.by("book.modifiedDate").descending());
    }
}
