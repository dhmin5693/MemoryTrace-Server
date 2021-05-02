package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.InviteSaveRequestDto;
import com.memorytrace.service.InviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "초대 관련 API", tags = "Invite")
@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @ApiOperation(value = "초대 코드를 이용하여 사용자 초대하기")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "사용자 초대 완료")
    })
    @PostMapping
    // TODO: 토큰 방식으로 추후 수정될 수 있음
    public ResponseEntity<DefaultRes> invite(@RequestBody InviteSaveRequestDto request) {
        inviteService.save(request);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_BOOK),
            HttpStatus.CREATED);
    }
}
