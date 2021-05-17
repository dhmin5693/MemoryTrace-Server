package com.memorytrace.dto.request;

import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "User 생성 요청")
public class UserSaveRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "회원 닉네임")
    private String nickname;

    @NotBlank(message = "sns key는 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "회원 SNS KEY")
    private String snsKey;

    @Builder
    public UserSaveRequestDto(String nickname, String snsKey) {
        this.nickname = nickname;
        this.snsKey = snsKey;
    }

    public User toEntity(String imgUrl) {
        return User.ByUserBuilder()
            .nickname(nickname)
            .snsKey(snsKey)
            .profileImg(imgUrl)
            .build();
    }
}
