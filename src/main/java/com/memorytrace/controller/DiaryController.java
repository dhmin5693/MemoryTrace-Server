package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.DiarySaveRequestDto;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "Diary 관련 API", tags = "Diary")
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "Diary 생성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Diary 생성 성공")
    })
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<DefaultRes> save(@ModelAttribute @Valid DiarySaveRequestDto requestDto,
        @RequestPart(value = "img") MultipartFile file) throws IOException {
        diaryService.save(requestDto, file);
        return new ResponseEntity(
            DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_DIARY), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Diary List 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Diary List 조회 성공")
    })
    @GetMapping("/list/{bid}")
    public ResponseEntity<DefaultRes> findByBook_BidOrderByModifiedDateDesc(
        @PathVariable Long bid) {
        DiaryListResponseDto diaryList = diaryService.findByBook_BidOrderByModifiedDateDesc(bid);
        return new ResponseEntity(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DIARY_LIST, diaryList),
            HttpStatus.OK);
    }

    @ApiOperation(value = "Diary 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Diary 조회 성공")
    })
    @GetMapping("/{did}")
    public ResponseEntity<DefaultRes> findBydid(@PathVariable Long did) {
        DiaryDetailResponseDto diary = diaryService.findByDid(did);
        return new ResponseEntity(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DIARY_DETAIL, diary), HttpStatus.OK);
    }
}
