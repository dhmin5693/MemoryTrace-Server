package com.memorytrace.dto.request;

import com.memorytrace.domain.FcmToken;
import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "FCM 토큰 저장 요청")
public class FcmSaveRequestDto {

    @NotNull(message = "uid는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "uid")
    private Long uid;

    @NotNull(message = "token은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "token")
    private String token;

    public FcmToken toEntity() {
        return FcmToken.builder()
            .user(new User(uid))
            .token(token)
            .build();
    }
}
