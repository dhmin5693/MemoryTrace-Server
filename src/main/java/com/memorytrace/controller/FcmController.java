package com.memorytrace.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.FcmDeleteRequestDto;
import com.memorytrace.dto.request.Message;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.service.FcmService;
import com.memorytrace.service.FirebaseMessagingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "토큰 관련 API", tags = "토큰")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    private final BookRepository bookRepository;

    private final FirebaseMessagingService firebaseMessagingService;

    // TODO: 추후 삭제
    @ApiOperation(value = "해당 토큰으로 메시지 보내기")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "해당 토큰으로 메시지 보내기")
    })
    @PostMapping("/test")
    public ResponseEntity<DefaultRes> message(@RequestBody @Valid FcmDeleteRequestDto request)
        throws FirebaseMessagingException {
        Book book = bookRepository.findByBid((long) 1).orElseThrow(
            () -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + 1));
        firebaseMessagingService.sendMulticast(
            Message.builder().subject("TEST")
                .content("TEST").
                data(book).build(),
            Arrays.asList(request.getToken()));
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.OK, "메시지 보내기 성공"), HttpStatus.OK);
    }

    @ApiOperation(value = "로그아웃 API 호출 전, 해당 기기 토큰 지우기")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "해당 기기 토큰 지우기 완료")
    })
    @DeleteMapping
    public ResponseEntity<DefaultRes> delete(@RequestBody @Valid FcmDeleteRequestDto request) {
        fcmService.deleteByFcmToken(request.getToken());
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_TOKEN), HttpStatus.OK);
    }
}
