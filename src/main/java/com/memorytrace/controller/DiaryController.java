package com.memorytrace.controller;

import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Diary 관련 API", tags = "Diary")
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "DiaryList 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "DiaryList 조회 성공")
    })
    @GetMapping("/list/{bid}")
    public List<DiaryListResponseDto> findByBook_BidOrderByModifiedDateDesc(
        @PathVariable Long bid) {
        return diaryService.findByBook_BidOrderByModifiedDateDesc(bid);
    }

    @ApiOperation(value = "Diary 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Diary 조회 성공")
    })
    @GetMapping("/{did}")
    public DiaryDetailResponseDto findBydid(@PathVariable Long did) {
        return diaryService.findByDid(did);
    }
}
