package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@ApiModel(value = "사용자 정보 조회 응답값")
public class UserDetailResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "사용자 고유 id")
    private Long uid;

    @ApiModelProperty(position = 2, required = true, value = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(position = 3, required = true, value = "가입 SNS")
    private String snsType;

    @ApiModelProperty(position = 4, required = true, value = "사용자 프로필 사진")
    private String profileImg;

    @ApiModelProperty(position = 5, required = true, value = "가입일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @ApiModelProperty(position = 6, required = true, value = "jwt")
    private String jwt;

    public UserDetailResponseDto(User entity, String jwt) {
        this.uid = entity.getUid();
        this.nickname = entity.getNickname();
        this.snsType = entity.getSnsKey().split("_")[0];
        this.profileImg = entity.getProfileImg();
        this.createdDate = entity.getCreatedDate();
        this.jwt = jwt;
    }

}
