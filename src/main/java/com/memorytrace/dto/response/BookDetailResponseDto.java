package com.memorytrace.dto.response;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.UserBook;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Book 조회 요청")
public class BookDetailResponseDto {

    @ApiModelProperty(position = 1, required = true)
    private Long bid;

    @ApiModelProperty(position = 2, required = true)
    private Long whoseTurn;

    @ApiModelProperty(position = 3, required = true)
    private String inviteCode;

    @ApiModelProperty(position = 4, required = true)
    private String createdDate;

    @ApiModelProperty(position = 5, required = true)
    private List<UsersInBook> userList;

    @Getter
    public class UsersInBook {

        private Long uid;
        private String nickname;
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
        this.createdDate = entity.getCreatedDate().toString();
        this.userList = userList;
    }
}
