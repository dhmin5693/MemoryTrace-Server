package com.memorytrace.controller;

import com.memorytrace.common.S3Uploder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "이미지 업로드 관련 API", tags = "Upload")
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ImgUploadController {

    final private S3Uploder s3Uploder;

    @ApiOperation(value = "프로필 이미지 업로드 시 해당 API 사용")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "프로필 이미지 링크 생성 완료")
    })
    @PostMapping("/profile-img")
    public ResponseEntity<String> uploadProfileImg(@RequestParam("files") MultipartFile files)
        throws IOException {
        return ResponseEntity.ok(s3Uploder.upload(files, "profile"));
    }

    @ApiOperation(value = "다이어리 이미지 업로드 시 해당 API 사용")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "다이어리 이미지 링크 생성 완료")
    })
    @PostMapping("/diary-img")
    public ResponseEntity<String> uploadDiaryImg(@RequestParam("files") MultipartFile files)
        throws IOException {
        return ResponseEntity.ok(s3Uploder.upload(files, "diary"));
    }

    @ApiOperation(value = "스티커 배경 이미지 업로드 시 해당 API 사용")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "스티커 배경 이미지 링크 생성 완료")
    })
    @PostMapping("/sticker-img")
    public ResponseEntity<String> uploadStickerImg(@RequestParam("files") MultipartFile files)
        throws IOException {
        return ResponseEntity.ok(s3Uploder.upload(files, "sticker"));
    }
}
