package com.memorytrace.dto.response;

import com.memorytrace.domain.User;
import lombok.Getter;

@Getter
public class UserDetailResponseDto {

    private Long uid;
    private String nickName;
    //    private String snsKey;
    private String profileImg;
    // private  // 순서
//    private String createDate;
//    private LocalDateTime modifiedDate;
//    private byte isWithdrawal;

    public UserDetailResponseDto(User entity) {
        this.uid = entity.getUid();
        this.nickName = entity.getNickname();
//        this.snsKey = entity.getSnsKey();
        this.profileImg = entity.getProfileImg();
//        this.createDate = entity.getProfileImg();
//        this.modifiedDate = entity.getModifiedDate();
//        this.isWithdrawal = entity.getIsWithdrawal();
    }

}
