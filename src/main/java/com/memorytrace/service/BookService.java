package com.memorytrace.service;

import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.UserBookRepository;
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
        long bid = bookRepository.save(request.toEntity()).getBid();
        UserBook userBook = UserBook.builder()
            .uid(request.getWhoseTurn())
            .bid(bid)
            .build();
        userBookRepository.save(userBook);
    }
}
