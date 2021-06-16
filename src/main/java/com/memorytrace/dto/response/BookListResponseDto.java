package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

        @ApiModelProperty(position = 6, value = "현재 교환일기 참여자 수")
        private int numOfPeople;

        @ApiModelProperty(position = 7, required = true, value = "교환 일기장 수정 날짜")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime modifiedDate;

        public BookList(Map<String, Object> entity) {
            this.bid = Long.parseLong(entity.get("bid").toString());
            this.nickname = entity.get("nickname").toString();
            this.title = entity.get("title").toString();
            this.bgColor = Byte.parseByte(entity.get("bg_color").toString());
            this.stickerImg =
                entity.get("sticker_img") != null ? entity.get("sticker_img").toString() : null;
            this.numOfPeople = Byte.parseByte(entity.get("num_of_people").toString());
            this.modifiedDate = LocalDateTime
                .parse(entity.get("modified_date").toString().replace(' ', 'T'));
        }
    }

    public BookListResponseDto(Page page, List<BookList> bookList) {
        super(page);
        this.bookList = bookList;
    }
}
