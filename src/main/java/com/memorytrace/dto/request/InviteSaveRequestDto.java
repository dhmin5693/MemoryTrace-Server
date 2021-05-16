package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Invite User 생성 요청")
public class InviteSaveRequestDto {

    @NotNull(message = "초대코드는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "초대 코드")
    private String inviteCode;

    @NotNull(message = "초대할 유저 id는 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "Long", value = "초대될 아이디")
    private Long uid;

    @Builder
    public InviteSaveRequestDto(String inviteCode, Long uid) {
        this.inviteCode = inviteCode;
        this.uid = uid;
    }
}
