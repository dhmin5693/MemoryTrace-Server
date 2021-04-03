package com.memorytrace.dto.request;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "Book 생성 요청")
public class BookSaveRequestDto {

    @NotNull
    @ApiModelProperty(position = 1, required = true, dataType = "long", value = "현재 차례인 uid")
    private long whoseTurn;

    @NotNull
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "book 제목")
    private String title;

    @NotNull
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "book 배경색")
    private byte bgColor;

    @NotNull
    @ApiModelProperty(position = 4, required = true, dataType = "String", value = "삭제여부")
    private byte isDelete;

    @Builder
    public BookSaveRequestDto(long whoseTurn, String title, byte bgColor, byte isDelete) {
        this.whoseTurn = whoseTurn;
        this.title = title;
        this.bgColor = bgColor;
        this.isDelete = isDelete;
    }

    public Book toEntity() {
        return Book.builder()
            .user(new User(whoseTurn))
            .title(title)
            .inviteCode(UUID.randomUUID().toString())
            .bgColor(bgColor)
            .isDelete(isDelete)
            .build();
    }
}
