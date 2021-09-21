package com.memorytrace.dto.response;

import com.memorytrace.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;

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

    @ApiModelProperty(position = 4, required = true, value = "댓글 작성 시간")
    private String createdDate;

    @ApiModelProperty(position = 5, required = true, value = "삭제 여부")
    private Byte isDelete;

    @ApiModelProperty(position = 6, required = true, value = "대댓글 리스트")
    private List<CommentList> commentList = new ArrayList<>();

    @Getter
    public class CommentList {

        @ApiModelProperty(position = 1, required = true, value = "댓글 고유 id")
        private Long cid;

        @ApiModelProperty(position = 2, required = true, value = "댓글 작성자 닉네임")
        private String nickname;

        @ApiModelProperty(position = 3, required = true, value = "댓글 내용")
        private String content;

        @ApiModelProperty(position = 4, required = true, value = "댓글 작성 시간")
        private String createdDate;

        @ApiModelProperty(position = 5, required = true, value = "삭제 여부")
        private Byte isDelete;

        public CommentList(Comment entity) {
            this.cid = entity.getCid();
            this.nickname = entity.getUser().getNickname();
            this.content = entity.getContent();
            this.createdDate = new PrettyTime().format(entity.getCreatedDate());
            this.isDelete = entity.getIsDelete();
        }
    }

    public CommentListResponseDto(Comment comment, List<CommentList> commentList) {
        this.cid = comment.getCid();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdDate = new PrettyTime().format(comment.getCreatedDate());
        this.isDelete = comment.getIsDelete();
        this.commentList = commentList;
    }
}
