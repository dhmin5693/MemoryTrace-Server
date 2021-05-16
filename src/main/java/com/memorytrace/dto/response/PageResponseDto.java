package com.memorytrace.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class PageResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "현재 페이지")
    private int curPage;

    @ApiModelProperty(position = 2, required = true, value = "다음 페이지 유무(true: 있음, false: 없음)")
    private boolean hasNext;

    public PageResponseDto(Page response) {
        int totalPage = response.getTotalPages();
        this.curPage = response.getPageable().getPageNumber() + 1;
        this.hasNext = curPage != totalPage;
    }
}
