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
@ApiModel(value = "교환 일기장 생성 요청")
public class BookSaveRequestDto {

    @ApiModelProperty(position = 1, dataType = "Long", value = "생성자 uid", hidden = true)
    private Long whoseTurn;

    @NotNull(message = "교환 일기장 제목은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "교환 일기장 제목")
    private String title;

    @ApiModelProperty(position = 3, required = true, dataType = "Byte", value = "교환 일기장 배경 색상")
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
