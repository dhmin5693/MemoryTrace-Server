package com.memorytrace.dto.request;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
import com.memorytrace.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "Diary 생성 요청")
public class DiarySaveRequestDto {

    @NotNull
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "다이어리 북 아이디")
    private Long bid;

    @NotNull
    @ApiModelProperty(position = 2, required = true, dataType = "Long", value = "사용자 아이디 고유번호")
    private Long uid;

    @NotNull
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "다이어리 제목")
    private String title;

    @ApiModelProperty(position = 5, dataType = "String", value = "다이어리 내용")
    private String content;

    @NotNull
    @ApiModelProperty(position = 8, required = true, dataType = "Byte", value = "배경색상")
    private Byte bgColor;

    @NotNull
    @ApiModelProperty(position = 9, required = true, dataType = "Byte", value = "속지 템플릿(default = 0)")
    private Byte template;

    public Diary toEntity(String imgUrl) {
        return Diary.ByDiaryBuilder()
            .title(title)
            .book(new Book(bid))
            .user(new User(uid))
            .img(imgUrl)
            .content(content)
            .bgColor(bgColor)
            .template(template)
            .build();
    }

}
