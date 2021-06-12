package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.memorytrace.domain.UserBook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@ApiModel(value = "교환 일기장 조회 응답값")
public class BookListResponseDto extends PageResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "교환 일기장 리스트")
    private List<BookList> bookList;

    @Getter
    public class BookList {

        @ApiModelProperty(position = 1, required = true, value = "교환 일기장 고유 아이디")
        private Long bid;

        @ApiModelProperty(position = 2, required = true, value = "현재 차례의 닉네임")
        private String nickname;

        @ApiModelProperty(position = 3, required = true, value = "교환 일기장 제목")
        private String title;

        @ApiModelProperty(position = 4, required = true, value = "교환 일기장 배경 색상")
        private Byte bgColor;

        @ApiModelProperty(position = 5, value = "스티커 이미지")
        private String stickerImg;

        @ApiModelProperty(position = 6, required = true, value = "교환 일기장 수정 날짜")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime modifiedDate;

        public BookList(UserBook entity) {
            this.bid = entity.getBid();
            this.nickname = entity.getBook().getUser().getNickname();
            this.title = entity.getBook().getTitle();
            this.bgColor = entity.getBook().getBgColor();
            this.stickerImg = entity.getBook().getStickerImg();
            this.modifiedDate = entity.getBook().getModifiedDate();
        }
    }

    public BookListResponseDto(Page page, List<BookList> bookList) {
        super(page);
        this.bookList = bookList;
    }
}
