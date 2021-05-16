package com.memorytrace.dto.request;

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
@ApiModel(value = "User 생성 요청")
public class UserSaveRequestDto {

    @NotNull(message = "닉네임은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "회원 닉네임")
    private String nickname;

    @NotNull(message = "sns key는 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "회원 SNS KEY")
    private String snsKey;

    @NotNull(message = "sns type은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "SnsType", value = "SNS TYPE : GOOGLE, KAKAO, APPLE")
    private SnsType snsType;

    public User toEntity(String imgUrl) {
        return User.ByUserBuilder()
            .nickname(nickname)
            .snsKey(snsKey)
            .profileImg(imgUrl)
            .build();
    }
}
