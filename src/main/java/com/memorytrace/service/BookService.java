package com.memorytrace.service;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.User;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.BookSaveRequestDto;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.UserBookRepository;
import java.util.UUID;
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
        Book book = Book.builder()
            .user(new User(request.getWhoseTurn()))
            .title(request.getTitle())
            .bgColor(request.getBgColor())
            .inviteCode(UUID.randomUUID().toString())
            .isDelete(request.getBgColor())
            .build();
        long bid = bookRepository.save(book).getBid();
        UserBook userBook = UserBook.builder()
            .uid(request.getWhoseTurn())
            .bid(bid)
            .build();
        userBookRepository.save(userBook);
    }
}
