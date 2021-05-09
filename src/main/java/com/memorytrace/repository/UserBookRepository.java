package com.memorytrace.repository;

import com.memorytrace.domain.UserBook;
import com.memorytrace.domain.UserBookPK;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookPK> {

    Page<UserBook> findByUidAndIsWithdrawal(Long uid, Byte isWithdrawal, Pageable pageable);

    List<UserBook> findByBidAndIsWithdrawal(Long bid, Byte isWithdrawal);

    List<UserBook> findByBidAndIsWithdrawalOrderByTurnNo(Long bid, Byte isWithdrawal);
}
