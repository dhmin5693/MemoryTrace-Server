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
    @ApiModelProperty(position = 1, required = true, value = "현재 차례인 uid (생성자 uid)")
    private Long whoseTurn;

    @NotNull
    @ApiModelProperty(position = 2, required = true)
    private String title;

    @NotNull
    @ApiModelProperty(position = 3, required = true)
    private Byte bgColor;

    @Builder
    public BookSaveRequestDto(Long whoseTurn, String title, Byte bgColor) {
        this.whoseTurn = whoseTurn;
        this.title = title;
        this.bgColor = bgColor;
    }

    public Book toEntity() {
        return Book.builder()
            .user(new User(whoseTurn))
            .title(title)
            .bgColor(bgColor)
            .inviteCode(UUID.randomUUID().toString())
            .build();
    }
}
