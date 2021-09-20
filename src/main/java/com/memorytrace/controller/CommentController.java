package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.CommentSaveRequestDto;
import com.memorytrace.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "댓글 관련 API", tags = "댓글")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "교환 일기장 생성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "교환 일기장 생성 성공")
    })
    @PostMapping
    public ResponseEntity<DefaultRes> save(CommentSaveRequestDto requestDto) {
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_COMMENT,
                commentService.save(requestDto)), HttpStatus.CREATED);
    }
}
