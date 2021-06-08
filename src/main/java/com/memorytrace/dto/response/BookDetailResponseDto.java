package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.UserBook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "일기장 설정 페이지 응답값(일기장 상세 조회)")
public class BookDetailResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "교환 일기장 고유 아이디")
    private Long bid;

    @ApiModelProperty(position = 2, required = true, value = "현재 차례의 uid")
    private Long whoseTurn;

    @ApiModelProperty(position = 3, required = true, value = "교환 일기장 초대 코드")
    private String inviteCode;

    @ApiModelProperty(position = 4, required = true, value = "교환 일기장 제목")
    private String title;

    @ApiModelProperty(position = 5, required = true, value = "교환 일기장 배경 색상")
    private Byte bgColor;

    @ApiModelProperty(position = 6, value = "스티커 이미지")
    private String stickerImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(position = 7, required = true, value = "교환 일기장 생성날짜")
    private LocalDateTime createdDate;

    @ApiModelProperty(position = 8, required = true, value = "해당 교환 일기장 초대멤버 리스트")
    private List<UsersInBook> userList;

    @Getter
    public class UsersInBook {

        @ApiModelProperty(position = 1, required = true, value = "회원 고유 아이디")
        private Long uid;

        @ApiModelProperty(position = 2, required = true, value = "닉네임")
        private String nickname;

        @ApiModelProperty(position = 3, required = true, value = "유저 프로필 사진")
        private String profileImg;

        public UsersInBook(UserBook entity) {
            this.uid = entity.getUser().getUid();
            this.nickname = entity.getUser().getNickname();
            this.profileImg = entity.getUser().getProfileImg();
        }
    }

    public BookDetailResponseDto(Book entity, List<UsersInBook> userList) {
        this.bid = entity.getBid();
        this.whoseTurn = entity.getUser().getUid();
        this.inviteCode = entity.getInviteCode();
        this.title = entity.getTitle();
        this.bgColor = entity.getBgColor();
        this.stickerImg = entity.getStickerImg();
        this.createdDate = entity.getCreatedDate();
        this.userList = userList;
    }
}
