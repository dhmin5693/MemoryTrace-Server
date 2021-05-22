package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "일기장 조회 요청")
public class DiaryDetailResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "일기 고유 아이디")
    private Long did;

    @ApiModelProperty(position = 2, required = true, value = "일기 작성자 닉네임")
    private String nickname;

    @ApiModelProperty(position = 3, required = true, value = "일기 제목")
    private String title;

    @ApiModelProperty(position = 4, required = true, value = "일기 첨부 이미지")
    private String img;

    @ApiModelProperty(position = 5, required = true, value = "일기 내용")
    private String content;

    @ApiModelProperty(position = 6, required = true, value = "일기 속지 템플릿(default = 0)")
    private Byte template;

    @ApiModelProperty(position = 7, required = true, value = "작성 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;

    public DiaryDetailResponseDto(Diary entity) {
        this.did = entity.getDid();
        this.nickname = entity.getUser().getNickname();
        this.title = entity.getTitle();
        this.img = entity.getImg();
        this.content = entity.getContent();
        this.template = entity.getTemplate();
        this.createdDate = entity.getCreatedDate();
    }
}
