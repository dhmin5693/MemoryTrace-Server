package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.FcmDeleteRequestDto;
import com.memorytrace.service.FcmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "토큰 관련 API", tags = "토큰")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

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
