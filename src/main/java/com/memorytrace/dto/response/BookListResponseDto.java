package com.memorytrace.dto.response;

import com.memorytrace.domain.UserBook;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookListResponseDto {

    private Long bid;
    private String nickname;
    private String title;
    private Byte bgColor;
    private String modifiedDate;

    public BookListResponseDto(UserBook entity) {
        this.bid = entity.getBid();
        this.nickname = entity.getUser().getNickname();
        this.title = entity.getBook().getTitle();
        this.bgColor = entity.getBook().getBgColor();
        this.modifiedDate = entity.getBook().getModifiedDate().toString();
    }
}
