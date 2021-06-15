package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.DiarySaveRequestDto;
import com.memorytrace.dto.request.PageRequestDto;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.dto.response.DiarySaveResponseDto;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "일기 관련 API", tags = "일기")
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "일기 작성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "일기 작성 성공")
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DefaultRes<DiarySaveResponseDto>> save(@ModelAttribute @Valid DiarySaveRequestDto requestDto,
        @RequestPart(value = "img", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_DIARY,
                diaryService.save(requestDto, file)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "교환일기장 상세(일기 리스트)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "교환일기장 상세(일기 리스트) 조회 성공")
    })
    @GetMapping("/list/{bid}")
    public ResponseEntity<DefaultRes<DiaryListResponseDto>> findByBook_Bid(
        @PathVariable Long bid, PageRequestDto pageRequestDto) {
        DiaryListResponseDto diaryList = diaryService.findByBook_Bid(bid, pageRequestDto);
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DIARY_LIST, diaryList),
            HttpStatus.OK);
    }

    @ApiOperation(value = "일기 확인")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "일기 확인 성공")
    })
    @GetMapping("/{did}")
    public ResponseEntity<DefaultRes<DiaryDetailResponseDto>> findBydid(@PathVariable Long did) {
        DiaryDetailResponseDto diary = diaryService.findByDid(did);
        return new ResponseEntity<>(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_DIARY_DETAIL, diary), HttpStatus.OK);
    }
}
