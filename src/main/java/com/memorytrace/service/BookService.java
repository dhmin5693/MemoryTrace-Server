package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.dto.response.BookListResponseDto;
import com.memorytrace.dto.response.BookSaveResponseDto;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.UserBookRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final S3Uploder s3Uploder;

    @Transactional
    public BookSaveResponseDto save(BookSaveRequestDto requestDto, MultipartFile file)
        throws IOException {
        String imgUrl = s3Uploder.upload(file, "book");
        Book book = bookRepository.save(requestDto.toEntity(imgUrl));
        UserBook userBook = UserBook.builder()
            .uid(requestDto.getWhoseTurn())
            .bid(book.getBid())
            .build();
        userBookRepository.save(userBook);
        return new BookSaveResponseDto(book);
    }

    @Transactional(readOnly = true)
    public List<BookListResponseDto> findByUidAndIsWithdrawal(Long uid) {
        List<UserBook> userBook = userBookRepository
            .findByUidAndIsWithdrawal(uid, (byte) 0);
        List<BookListResponseDto> list = new ArrayList<>();
        for (UserBook ub : userBook) {
            list.add(new BookListResponseDto(ub));
        }
        Collections.sort(list, (b1, b2) -> b2.getModifiedDate().compareTo(b1.getModifiedDate()));
        return list;
    }
}
