package com.memorytrace.dto.response;

import com.memorytrace.domain.UserBook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "BookList 조회 요청")
public class BookListResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long bid;

    @ApiModelProperty(position = 2, required = true)
    private String nickname;

    @ApiModelProperty(position = 3, required = true)
    private String title;

    @ApiModelProperty(position = 4, required = true)
    private Byte bgColor;

    @ApiModelProperty(position = 5)
    private String stickerImg;

    @ApiModelProperty(position = 6, required = true)
    private String modifiedDate;

    public BookListResponseDto(UserBook entity) {
        this.bid = entity.getBid();
        this.nickname = entity.getBook().getUser().getNickname();
        this.title = entity.getBook().getTitle();
        this.bgColor = entity.getBook().getBgColor();
        this.stickerImg = entity.getBook().getStickerImg();
        this.modifiedDate = entity.getBook().getModifiedDate().toString();
    }
}