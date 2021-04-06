package com.memorytrace.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Invite User 생성 요청")
public class InviteSaveRequestDto {

    @NotNull
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "초대 코드")
    private String inviteCode;

    @NotNull
    @ApiModelProperty(position = 2, required = true, dataType = "Long", value = "초대될 아이디")
    private Long uid;
}
