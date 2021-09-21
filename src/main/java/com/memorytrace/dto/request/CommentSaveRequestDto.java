package com.memorytrace.dto.request;

import com.memorytrace.domain.Comment;
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
@ApiModel(value = "댓글 작성 요청")
public class CommentSaveRequestDto {

    @ApiModelProperty(position = 1, dataType = "Long", value = "댓글 작성 시, 0 / 대댓글 작성 시 해당 댓글 고유번호")
    private Long parent;

    @NotNull(message = "did는 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "Long", value = "교환 일기장 고유 번호")
    private Long did;

    @ApiModelProperty(position = 2, hidden = true, dataType = "Long", value = "사용자 고유 번호")
    private Long uid;

    @NotNull(message = "content는 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "댓글 작성 내용")
    private String content;

    public Comment toEntity() {
        return Comment.builder()
            .parent(parent)
            .user(new User(uid))
            .diary(new Diary(did))
            .content(content)
            .build();
    }
}
