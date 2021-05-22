package com.memorytrace.dto.request;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "교환 일기장 수정 요청")
public class BookUpdateRequestDto {

    @NotNull(message = "Bid는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "교환 일기장 고유 번호")
    private Long bid;

    @NotNull(message = "교환 일기장 제목은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "교환 일기장 제목")
    private String title;

    @ApiModelProperty(position = 3, required = true, dataType = "Byte", value = "교환 일기장 배경 색상")
    private Byte bgColor;

    public Book toEntity(String imgUrl, Book book) {
        return Book.UpdateBuilder()
            .bid(bid)
            .user(new User(book.getUser().getUid()))
            .title(title)
            .bgColor(bgColor)
            .stickerImg(imgUrl)
            .isDelete(book.getIsDelete())
            .inviteCode(book.getInviteCode())
            .build();
    }
}
