package com.memorytrace.controller;

import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "User 관련 API", tags = "User")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "사용자 생성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "사용자 생성 완료")
    })
    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserSaveRequestDto request) {
        userService.save(request);
        return ResponseEntity.ok("OK");
    }
}
