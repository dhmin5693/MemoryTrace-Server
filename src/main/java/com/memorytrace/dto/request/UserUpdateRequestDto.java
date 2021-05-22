package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "User 닉네임 수정 요청")
public class UserUpdateRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "닉네임")
    private String nickname;
}
