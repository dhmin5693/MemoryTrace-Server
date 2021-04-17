package com.memorytrace.dto.response;

import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(value = "DiaryList 조회 요청")
public class DiaryListResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long did;

    @ApiModelProperty(position = 2, required = true)
    private Long whoseTurn;

    @ApiModelProperty(position = 3, required = true)
    private String nickname;

    @ApiModelProperty(position = 4, required = true)
    private String title;

    @ApiModelProperty(position = 5, required = true)
    private String img;

    @ApiModelProperty(position = 6, required = true)
    private Byte template;

    @ApiModelProperty(position = 7, required = true)
    private String modifiedDate;

    public DiaryListResponseDto(Diary entity) {
        this.did = entity.getDid();
        this.whoseTurn = entity.getBook().getUser().getUid();
        this.nickname = entity.getUser().getNickname();
        this.title = entity.getTitle();
        this.img = entity.getImg();
        this.template = entity.getTemplate();
        this.modifiedDate = entity.getModifiedDate().toString();
    }
}
