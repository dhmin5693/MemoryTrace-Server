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

    @ApiModelProperty(position = 2, required = true, value = "일기 작성자 uid")
    private Long uid;

    @ApiModelProperty(position = 3, required = true, value = "일기 작성자 닉네임")
    private String nickname;

    @ApiModelProperty(position = 4, required = true, value = "일기 제목")
    private String title;

    @ApiModelProperty(position = 5, required = true, value = "일기 첨부 이미지")
    private String img;

    @ApiModelProperty(position = 6, required = true, value = "일기 내용")
    private String content;

    @ApiModelProperty(position = 7, required = true, value = "일기 수정 가능 여부")
    private boolean isModifiable;

    @ApiModelProperty(position = 8, required = true, value = "일기 속지 템플릿(default = 0)")
    private Byte template;

    @ApiModelProperty(position = 9, required = true, value = "작성 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @ApiModelProperty(position = 10, required = true, value = "작성된 댓글 개수")
    private Long commentCnt;

    public DiaryDetailResponseDto(Diary entity, boolean isModifiable, Long commentCnt) {
        this.did = entity.getDid();
        this.uid = entity.getUser().getUid();
        this.isModifiable = isModifiable;
        this.nickname = entity.getUser().getNickname();
        this.title = entity.getTitle();
        this.img = entity.getImg();
        this.content = entity.getContent();
        this.template = entity.getTemplate();
        this.createdDate = entity.getCreatedDate();
        this.commentCnt = commentCnt;
    }
}
