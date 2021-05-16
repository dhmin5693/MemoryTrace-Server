package com.memorytrace.dto.response;

import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "일기 생성 응답값")
public class DiarySaveResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "일기 고유 아이디")
    private Long did;

    public DiarySaveResponseDto(Diary entity) {
        this.did = entity.getDid();
    }
}
