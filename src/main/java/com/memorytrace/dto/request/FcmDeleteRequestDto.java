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
@ApiModel(value = "FCM 토큰 삭제 요청")
public class FcmDeleteRequestDto {
    @NotNull(message = "token값은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "해당 기기 토큰")
    private String token;
}
