package com.memorytrace.dto.response;

import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@ApiModel(value = "DiaryList 조회 요청")
public class DiaryListResponseDto extends PageResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long whoseTurn;

    @ApiModelProperty(position = 2, required = true)
    private List<DiaryList> diaryList;

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

    public DiaryListResponseDto(Page page, Long whoseTurn, List<DiaryList> diaryList) {
        super(page);
        this.whoseTurn = whoseTurn;
        this.diaryList = diaryList;
    }
}