package com.memorytrace.dto.response;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "DiaryList 조회 요청")
public class DiaryListResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private String title;

    @ApiModelProperty(position = 2, required = true)
    private Long whoseTurn;

    @ApiModelProperty(position = 3, required = true)
    private List<DiaryList> diaryList = new ArrayList<>();

    @Getter
    public class DiaryList {

        private Long did;
        private String nickname;
        private String title;
        private String img;
        private Byte template;
        private String modifiedDate;

        public DiaryList(Diary entity) {
            this.did = entity.getDid();
            this.nickname = entity.getUser().getNickname();
            this.title = entity.getTitle();
            this.img = entity.getImg();
            this.template = entity.getTemplate();
            this.modifiedDate = entity.getModifiedDate().toString();
        }
    }

    public DiaryListResponseDto(Book entity, List<DiaryList> diaryList) {
        this.title = entity.getTitle();
        this.whoseTurn = entity.getUser().getUid();
        this.diaryList = diaryList;
    }
}