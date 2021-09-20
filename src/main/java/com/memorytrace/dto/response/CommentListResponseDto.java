package com.memorytrace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.memorytrace.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "댓글 리스트 조회 응답값")
public class CommentListResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "댓글 고유 id")
    private Long cid;

    @ApiModelProperty(position = 2, required = true, value = "댓글 작성자 닉네임")
    private String nickname;

    @ApiModelProperty(position = 3, required = true, value = "댓글 내용")
    private String content;

    // TODO : 라이브러리 적용
    @ApiModelProperty(position = 4, required = true, value = "댓글 작성 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @ApiModelProperty(position = 5, required = true, value = "대댓글 리스트")
    private List<CommentList> commentList = new ArrayList<>();

    @Getter
    public class CommentList {

        @ApiModelProperty(position = 1, required = true, value = "댓글 고유 id")
        private Long cid;

        @ApiModelProperty(position = 2, required = true, value = "댓글 작성자 닉네임")
        private String nickname;

        @ApiModelProperty(position = 3, required = true, value = "댓글 내용")
        private String content;

        // TODO : 라이브러리 적용
        @ApiModelProperty(position = 4, required = true, value = "댓글 작성 시간")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;

        public CommentList(Comment entity) {
            this.cid = entity.getCid();
            this.nickname = entity.getUser().getNickname();
            this.content = entity.getContent();
            this.createdDate = entity.getCreatedDate();
        }
    }

    public CommentListResponseDto(Comment comment, List<CommentList> commentList) {
        this.cid = comment.getCid();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
        this.commentList = commentList;
    }
}
