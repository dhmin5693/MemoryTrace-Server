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
@ApiModel(value = "일기장 생성 요청")
public class DiarySaveRequestDto {

    @NotNull(message = "bid는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "교환 일기장 고유 시퀀스 아이디")
    private Long bid;

    @NotNull(message = "생성자 uid는 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "Long", value = "생성자 uid")
    private Long uid;

    @NotNull(message = "일기장 제목 필수입니다.")
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "일기 제목")
    private String title;

    @ApiModelProperty(position = 4, dataType = "String", value = "일기 내용")
    private String content;

    public Diary toEntity(String imgUrl) {
        return Diary.ByDiaryBuilder()
            .title(title)
            .book(new Book(bid))
            .user(new User(uid))
            .img(imgUrl)
            .content(content)
            .build();
    }

}
