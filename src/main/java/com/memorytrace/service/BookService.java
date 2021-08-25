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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        Page<Map<String, Object>> userBook = userBookRepository
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
    public BookDetailResponseDto findByBid(Long bid) throws MethodArgumentNotValidException {
        Book book = bookRepository.findByBid(bid).orElseThrow(
            () -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(bid, "검색 되는 일기장이 없습니다. bid=" + bid)));

        try {
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

    @Transactional
    public void update(BookUpdateRequestDto requestDto, MultipartFile file)
        throws IOException, MethodArgumentNotValidException {
        String imgUrl = file == null ? null : s3Uploder.upload(file, "book");
        Book book = bookRepository.findByBid(requestDto.getBid()).orElseThrow(
            () -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(requestDto.getBid(),
                    "검색 되는 일기장이 없습니다. bid=" + requestDto.getBid())));
        try {
            book.update(requestDto, imgUrl);
        } catch (Exception e) {
            log.error("교환일기 수정 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void exitBook(Long bid) {
        try {
            Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal())
                .getUid();

            Optional<Book> book = bookRepository.findByBidAndUser_Uid(bid, uid);

            List<UserBook> userBookList = userBookRepository
                .findByBidAndIsWithdrawalOrderByTurnNo(bid, (byte) 0);

            int idx = userBookList.stream().map(d -> d.getUid())
                .collect(Collectors.toList()).indexOf(uid);

            if (book.isPresent()) {
                if (userBookList.size() == 1) {
                    book.get().delete();
                } else {
                    User nextUser = idx == userBookList.size() - 1
                        ? userBookList.get(0).getUser() : userBookList.get(idx + 1).getUser();
                    book.get().updateWhoseTurnBook(bid, nextUser);
                }
            }

            userBookList.get(idx).exit();
        } catch (Exception e) {
            log.error("다이어리 나가기 중 에러 발생", e);
            throw new MemoryTraceException();
        }
    }
}
