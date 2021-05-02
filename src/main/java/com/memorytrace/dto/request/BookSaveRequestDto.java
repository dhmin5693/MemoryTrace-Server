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
@ApiModel(value = "Book 생성 요청")
public class BookSaveRequestDto {

    @NotNull
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "현재 차례인 uid (생성자 uid)")
    private Long whoseTurn;

    @NotNull
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "Book 제목")
    private String title;

    @NotNull
    @ApiModelProperty(position = 3, required = true, dataType = "Byte", value = "Book 배경 색상")
    private Byte bgColor;

    public Book toEntity(String imgUrl) {
        return Book.builder()
            .user(new User(whoseTurn))
            .title(title)
            .bgColor(bgColor)
            .stickerImg(imgUrl)
            .inviteCode(UUID.randomUUID().toString())
            .build();
    }
}
