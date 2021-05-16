package com.memorytrace.controller;

import com.memorytrace.common.ResponseMessage;
import com.memorytrace.common.StatusCode;
import com.memorytrace.domain.DefaultRes;
import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.dto.request.PageRequestDto;
import com.memorytrace.dto.response.BookDetailResponseDto;
import com.memorytrace.dto.response.BookListResponseDto;
import com.memorytrace.service.BookService;
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

@Api(description = "교환 일기장 관련 API", tags = "교환 일기장")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @ApiOperation(value = "교환 일기장 생성")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "교환 일기장 생성 성공")
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DefaultRes> save(@ModelAttribute @Valid BookSaveRequestDto requestDto,
        @RequestPart(value = "stickerImg", required = false) MultipartFile file)
        throws IOException {
        return new ResponseEntity(DefaultRes
            .res(StatusCode.CREATED, ResponseMessage.CREATED_BOOK,
                bookService.save(requestDto, file)),
            HttpStatus.CREATED);
    }

    @ApiOperation(value = "교환 일기장 목록")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "교환 일기장 목록 조회 성공")
    })
    @GetMapping("/list/{uid}")
    public ResponseEntity<DefaultRes> findByUid(@PathVariable Long uid,
        PageRequestDto pageRequestDto) {
        BookListResponseDto bookList = bookService.findByUidAndIsWithdrawal(uid, pageRequestDto);
        return new ResponseEntity(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BOOK_LIST, bookList), HttpStatus.OK);
    }

    @ApiOperation(value = "일기장 설정 페이지(일기장 상세 조회)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "일기장 설정 조회 성공")
    })
    @GetMapping("/{bid}")
    public ResponseEntity<DefaultRes> findBybid(@PathVariable Long bid) {
        BookDetailResponseDto book = bookService.findByBid(bid);
        return new ResponseEntity(
            DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BOOK_DETAIL, book), HttpStatus.OK);
    }
}
