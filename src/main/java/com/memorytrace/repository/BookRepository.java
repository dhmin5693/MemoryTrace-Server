package com.memorytrace.repository;

import com.memorytrace.domain.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByInviteCode(String inviteCode);

    Book findByBid(Long bid);

    Optional<Book> findByBidAndUser_Uid(Long bid, Long whoseTurn);
}
