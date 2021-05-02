package com.memorytrace.dto.response;

import com.memorytrace.domain.Book;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Book 생성")
public class BookSaveResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long bid;

    public BookSaveResponseDto(Book entity) {
        this.bid = entity.getBid();
    }
}
