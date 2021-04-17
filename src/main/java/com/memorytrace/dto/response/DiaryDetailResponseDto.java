package com.memorytrace.dto.response;

import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Diary 조회 요청")
public class DiaryDetailResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long did;

    @ApiModelProperty(position = 2, required = true)
    private String nickname;

    @ApiModelProperty(position = 3, required = true)
    private String title;

    @ApiModelProperty(position = 4, required = true)
    private String img;

    @ApiModelProperty(position = 5, required = true)
    private String content;

    @ApiModelProperty(position = 6, required = true)
    private Byte bgColor;

    @ApiModelProperty(position = 7, required = true)
    private Byte template;

    public DiaryDetailResponseDto(Diary entity) {
        this.did = entity.getDid();
        this.nickname = entity.getUser().getNickname();
        this.title = entity.getTitle();
        this.img = entity.getImg();
        this.content = entity.getContent();
        this.bgColor = entity.getBgColor();
        this.template = entity.getTemplate();
    }
}
