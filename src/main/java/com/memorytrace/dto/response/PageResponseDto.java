package com.memorytrace.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class PageResponseDto {

    private int curPage;
    private boolean hasNext;

    public PageResponseDto(Page response) {
        int totalPage = response.getTotalPages();
        this.curPage = response.getPageable().getPageNumber() + 1;
        this.hasNext = !(curPage == totalPage);
    }
}
