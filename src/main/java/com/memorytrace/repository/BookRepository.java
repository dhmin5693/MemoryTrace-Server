package com.memorytrace.repository;

import com.memorytrace.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByInviteCode(String inviteCode);

    Book findByBid(Long bid);
}
