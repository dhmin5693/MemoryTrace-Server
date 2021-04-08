package com.memorytrace.dto.request;

import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "User 생성 요청")
public class UserSaveRequestDto {

    @NotNull
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "회원 닉네임")
    private String nickname;

    @NotNull
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "회원 SNS KEY")
    private String snsKey;

    @ApiModelProperty(position = 3, dataType = "String", value = "회원 프로필 사진")
    private String profileImg;

    @Builder
    public UserSaveRequestDto(String nickname, String snsKey, String profileImg) {
        this.nickname = nickname;
        this.snsKey = snsKey;
        this.profileImg = profileImg;
    }

    public User toEntity() {
        return User.ByUserBuilder()
            .nickname(nickname)
            .snsKey(snsKey)
            .profileImg(profileImg)
            .build();
    }
}
