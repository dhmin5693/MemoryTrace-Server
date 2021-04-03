package com.memorytrace.service;

import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.dto.response.BookListResponseDto;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.UserBookRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;

    @Transactional
    public void save(BookSaveRequestDto request) {
        Long bid = bookRepository.save(request.toEntity()).getBid();
        UserBook userBook = UserBook.builder()
            .uid(request.getWhoseTurn())
            .bid(bid)
            .build();
        userBookRepository.save(userBook);
    }

    @Transactional(readOnly = true)
    public List<BookListResponseDto> findByUidAndIsWithdrawal(Long uid) {
        List<UserBook> userBook = userBookRepository
            .findByUidAndIsWithdrawal(uid, new Byte((byte) 0));
        List<BookListResponseDto> list = new ArrayList<>();
        for (UserBook ub : userBook) {
            list.add(new BookListResponseDto(ub));
        }
        Collections.sort(list, new Comparator<BookListResponseDto>() {
            @Override
            public int compare(BookListResponseDto b1, BookListResponseDto b2) {
                return b2.getModifiedDate().compareTo(b1.getModifiedDate());
            }
        });
        return list;
    }
}
