package com.memorytrace.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "댓글 생성 응답값")
public class CommentSaveResponseDto {
    @ApiModelProperty(position = 1, required = true, value = "댓글 고유 번호")
    private Long cid;

    public CommentSaveResponseDto(Long cid) { this.cid = cid; }
}
