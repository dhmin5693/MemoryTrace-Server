package com.memorytrace.controller;

import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.dto.response.BookListResponseDto;
import com.memorytrace.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Book 관련 API", tags = "Book")
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @ApiOperation(value = "Book 생성")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Book 생성 완료")
    })
    @PostMapping
    public ResponseEntity save(@RequestBody @Valid BookSaveRequestDto request) {
        bookService.save(request);
        return ResponseEntity.ok("OK");
    }

    @ApiOperation(value = "BookList 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "BookList 조회 성공")
    })
    @GetMapping("/list/{uid}")
    public List<BookListResponseDto> findByUid(@PathVariable Long uid) {
        List<BookListResponseDto> list = bookService.findByUidAndIsWithdrawal(uid);
        return list;
    }
}
