package com.memorytrace.dto.response;

import com.memorytrace.domain.Book;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "교환일기장 생성 응답값")
public class BookSaveResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "교환 일기장 고유 번호")
    private Long bid;

    public BookSaveResponseDto(Book entity) {
        this.bid = entity.getBid();
    }
}
