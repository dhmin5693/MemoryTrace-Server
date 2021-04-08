package com.memorytrace.repository;

import com.memorytrace.domain.UserBook;
import com.memorytrace.domain.UserBookPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookPK> {

    List<UserBook> findByUidAndIsWithdrawal(Long uid, Byte isWithdrawal);

    List<UserBook> findByBidAndIsWithdrawal(Long bid, Byte isWithdrawal);

}
