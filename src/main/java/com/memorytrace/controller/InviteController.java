package com.memorytrace.controller;

import com.memorytrace.dto.request.InviteSaveRequestDto;
import com.memorytrace.service.InviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "초대 관련 API", tags = "Invite")
@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @ApiOperation(value = "초대 코드를 이용하여 사용자 초대하기")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "사용자 초대 완료")
    })
    @PostMapping
    // TODO: 토큰 방식으로 추후 수정될 수 있음
    public ResponseEntity invite(@RequestBody InviteSaveRequestDto request) {
        inviteService.save(request);
        return ResponseEntity.ok("OK");
    }
}
