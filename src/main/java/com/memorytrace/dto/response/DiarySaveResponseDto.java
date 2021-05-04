package com.memorytrace.dto.response;

import com.memorytrace.domain.Diary;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Diary 생성")
public class DiarySaveResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long did;

    public DiarySaveResponseDto(Diary entity) {
        this.did = entity.getDid();
    }
}
