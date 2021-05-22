package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.dto.request.UserUpdateRequestDto;
import com.memorytrace.dto.response.UserDetailResponseDto;
import com.memorytrace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "사용자 관련 API", tags = "사용자")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "사용자 생성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "사용자 생성 완료")
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DefaultRes<UserDetailResponseDto>> save(
        @ModelAttribute @Valid UserSaveRequestDto request,
        @RequestPart(value = "img", required = false) MultipartFile file) throws IOException {
        UserDetailResponseDto existingUser = userService.getExistingUser(request);
        if (existingUser != null) {
            return new ResponseEntity(
                DefaultRes.res(StatusCode.OK, ResponseMessage.EXISTING_USER, existingUser),
                HttpStatus.OK);
        }
        return new ResponseEntity(
            DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER,
                userService.save(request, file)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "사용자 정보 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "사용자 정보 조회 완료")
    })
    @GetMapping
    public ResponseEntity<DefaultRes<UserDetailResponseDto>> findById(
        @RequestHeader HttpHeaders headers) {
        return new ResponseEntity(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_DETAIL,
                userService.findById(headers)), HttpStatus.OK);
    }

    @ApiOperation(value = "사용자 닉네임 수정")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "사용자 닉네임 수정 완료")
    })
    @PutMapping
    public ResponseEntity<DefaultRes<UserDetailResponseDto>> updateNickname(
        @RequestHeader HttpHeaders headers, @RequestBody @Valid UserUpdateRequestDto request) {
        return new ResponseEntity(
            DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER_NICKNAME,
                userService.updateNickname(headers, request)), HttpStatus.OK);
    }

    @ApiOperation(value = "스웨거로 테스트 시 jwt 조회하는 API")
    @GetMapping("/jwt/{uid}")
    public ResponseEntity getToken(@PathVariable Long uid) {
        String jwt = userService.getToken(uid);
        return ResponseEntity.ok().body(jwt);
    }
}
