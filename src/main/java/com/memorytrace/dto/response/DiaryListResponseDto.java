package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
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
@ApiModel(value = "일기 리스트 조회 응답값")
public class DiaryListResponseDto extends PageResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "일기 제목")
    private String title;

    @ApiModelProperty(position = 2, required = true, value = "현재 차례 uid")
    private Long whoseTurn;

    @ApiModelProperty(position = 3, required = true, value = "일기 리스트")
    private List<DiaryList> diaryList = new ArrayList<>();

    @Getter
    public class DiaryList {

        @ApiModelProperty(position = 1, required = true, value = "일기 고유 id")
        private Long did;

        @ApiModelProperty(position = 2, required = true, value = "현재 차례인 닉네임")
        private String nickname;

        @ApiModelProperty(position = 1, required = true, value = "댓글 작성자 uid")
        private String title;

        @ApiModelProperty(position = 4, required = true, value = "일기 이미지")
        private String img;

        @ApiModelProperty(position = 5, required = true, value = "속지 템플릿(default = 0)")
        private Byte template;

        @ApiModelProperty(position = 6, required = true, value = "일기 작성 날짜")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;

        public DiaryList(Diary entity) {
            this.did = entity.getDid();
            this.nickname = entity.getUser().getNickname();
            this.title = entity.getTitle();
            this.img = entity.getImg();
            this.template = entity.getTemplate();
            this.createdDate = entity.getCreatedDate();
        }
    }

    public DiaryListResponseDto(Page page, Book book, List<DiaryList> diaryList) {
        super(page);
        this.title = book.getTitle();
        this.whoseTurn = book.getUser().getUid();
        this.diaryList = diaryList;
    }
}
