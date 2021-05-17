package com.memorytrace.dto.response;

import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(value = "사용자 정보 조회 응답값")
public class UserDetailResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "사용자 고유 id")
    private Long uid;

    @ApiModelProperty(position = 2, required = true, value = "사용자 닉네임")
    private String nickName;

    @ApiModelProperty(position = 3, required = true, value = "사용자 프로필 사진")
    private String profileImg;

    @ApiModelProperty(position = 4, required = true, value = "jwt")
    private String jwt;

    public UserDetailResponseDto(User entity, String jwt) {
        this.uid = entity.getUid();
        this.nickName = entity.getNickname();
        this.profileImg = entity.getProfileImg();
        this.jwt = jwt;
    }

}
