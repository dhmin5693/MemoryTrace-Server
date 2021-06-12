package com.memorytrace.repository;

import com.memorytrace.domain.UserBook;
import com.memorytrace.domain.UserBookPK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookPK> {

    @Query(value =
        "select * ,(select count(*) from user_book ub where ub.bid = b.bid and ub.is_withdrawal = 0) as rest_of_people_cnt "
            + "from user_book u left join book b on u.bid = b.bid where u.uid = :uid and u.is_withdrawal = :isWithdrawal "
            + "order by b.modified_date desc",
        nativeQuery = true)
    Page<UserBook> findByUidAndIsWithdrawal(@Param("uid") Long uid,
        @Param("isWithdrawal") Byte isWithdrawal, Pageable pageable);

    List<UserBook> findByUidAndIsWithdrawal(Long uid, Byte isWithdrawal);

    List<UserBook> findByBidAndIsWithdrawal(Long bid, Byte isWithdrawal);

    List<UserBook> findByBidAndIsWithdrawalOrderByTurnNo(Long bid, Byte isWithdrawal);

    Optional<UserBook> findByBidAndUidAndIsWithdrawal(Long bid, Long uid, Byte isWithdrawal);

}
