package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.User;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.dto.request.BookUpdateRequestDto;
import com.memorytrace.dto.request.PageRequestDto;
import com.memorytrace.dto.response.BookDetailResponseDto;
import com.memorytrace.dto.response.BookListResponseDto;
import com.memorytrace.dto.response.BookSaveResponseDto;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.UserBookRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final UserBookRepository userBookRepository;

    private final S3Uploder s3Uploder;

    @Transactional
    public BookSaveResponseDto save(BookSaveRequestDto requestDto, MultipartFile file)
        throws IOException {
        requestDto.setWhoseTurn(((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid());

        String imgUrl = file == null ? null : s3Uploder.upload(file, "book");
        try {
            Book book = bookRepository.save(requestDto.toEntity(imgUrl));
            UserBook userBook = UserBook.builder()
                .uid(requestDto.getWhoseTurn())
                .bid(book.getBid())
                .build();
            userBookRepository.save(userBook);

            return new BookSaveResponseDto(book);
        } catch (Exception e) {
            log.error("교환일기 저장 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public BookListResponseDto findByUidAndIsWithdrawal(PageRequestDto pageRequestDto) {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();
        Page<UserBook> userBook = userBookRepository
            .findByUidAndIsWithdrawal(uid, (byte) 0,
                pageRequestDto.getPageableWithBookSort(pageRequestDto));

        try {
            List<BookListResponseDto.BookList> bookLists = userBook.stream()
                .map(book -> new BookListResponseDto().new BookList(book))
                .collect(Collectors.toList());

            return new BookListResponseDto(userBook, bookLists);
        } catch (Exception e) {
            log.error("교환일기 조회 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public BookDetailResponseDto findByBid(Long bid) {
        try {
            Book book = bookRepository.findByBid(bid).orElseThrow(
                () -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + bid));

            List<BookDetailResponseDto.UsersInBook> userList = userBookRepository
                .findByBidAndIsWithdrawalOrderByTurnNo(bid, (byte) 0).stream()
                .map(ub -> new BookDetailResponseDto().new UsersInBook(ub))
                .collect(Collectors.toList());

            return new BookDetailResponseDto(book, userList);
        } catch (Exception e) {
            log.error("교환일기 조회 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    public void update(BookUpdateRequestDto requestDto, MultipartFile file)
        throws IOException {
        String imgUrl = file == null ? null : s3Uploder.upload(file, "book");

        try {
            Book book = bookRepository.findByBid(requestDto.getBid()).orElseThrow(
                () -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + requestDto.getBid()));

            bookRepository.save(requestDto.toEntity(imgUrl, book));
        } catch (Exception e) {
            log.error("교환일기 수정 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }
}
