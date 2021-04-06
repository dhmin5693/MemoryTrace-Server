package com.memorytrace.dto.response;

import com.memorytrace.domain.User;
import lombok.Getter;

@Getter
public class UserDetailResponseDto {

    private Long uid;
    private String nickName;
    private String profileImg;

    public UserDetailResponseDto(User entity) {
        this.uid = entity.getUid();
        this.nickName = entity.getNickname();
        this.profileImg = entity.getProfileImg();
    }

}
