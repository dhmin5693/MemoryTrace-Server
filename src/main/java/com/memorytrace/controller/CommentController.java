package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.CommentSaveRequestDto;
import com.memorytrace.dto.response.CommentSaveResponseDto;
import com.memorytrace.dto.response.CommentListResponseDto;
import com.memorytrace.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "댓글 관련 API", tags = "댓글")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "댓글 작성 성공")
    })
    @PostMapping
    public ResponseEntity<DefaultRes<CommentSaveResponseDto>> save(@RequestBody @Valid CommentSaveRequestDto requestDto)
        throws MethodArgumentNotValidException {
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_COMMENT,
                commentService.save(requestDto)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "댓글 삭제")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "댓글 삭제 성공")
    })
    @PutMapping("/{cid}")
    public ResponseEntity<DefaultRes> delete(
        @ApiParam(value = "댓글 고유 아이디", required = true) @PathVariable Long cid)
        throws MethodArgumentNotValidException {
        commentService.deleteComment(cid);
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMENT), HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 리스트")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "댓글 리스트 조회 성공")
    })
    @GetMapping("/list/{did}")
    public ResponseEntity<DefaultRes<List<CommentListResponseDto>>> findByBook_Bid(
        @PathVariable Long did) throws MethodArgumentNotValidException {
        List<CommentListResponseDto> commentList = commentService.findByDid(did);
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMENT_LIST, commentList),
            HttpStatus.OK);
    }
}
