package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "일기장 수정 요청")
public class DiaryUpdateRequestDto {

    @NotNull(message = "did는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "일기 고유 시퀀스 아이디")
    private Long did;

    @NotNull(message = "일기장 제목 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "일기 제목")
    private String title;

    @ApiModelProperty(position = 3, dataType = "String", value = "일기 내용")
    private String content;
}
