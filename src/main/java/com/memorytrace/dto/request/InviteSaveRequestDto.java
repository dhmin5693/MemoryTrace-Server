package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Invite User 생성 요청")
public class InviteSaveRequestDto {

    @NotBlank(message = "유효하지 않는 초대코드 입니다. 다시 시도해주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "초대 코드")
    private String inviteCode;

    @Builder
    public InviteSaveRequestDto(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
