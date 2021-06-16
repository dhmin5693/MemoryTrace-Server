package com.memorytrace.repository;

import com.memorytrace.domain.UserBook;
import com.memorytrace.domain.UserBookPK;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookPK> {

    @Query(value = "select b.*, u.nickname,\n"
        + "       (select count(*) from user_book ub where ub.bid = b.bid and ub.is_withdrawal = 0) as num_of_people\n"
        + "from user_book ub\n"
        + "left join book b on ub.bid = b.bid\n"
        + "left join user u on b.whose_turn = u.uid\n"
        + "where ub.uid = :uid and ub.is_withdrawal = :isWithdrawal\n"
        + "order by b.modified_date desc",
        countQuery = "select count(*) from user_book where uid = :uid and is_withdrawal = :isWithdrawal", nativeQuery = true)
    Page<Map<String, Object>> findByUidAndIsWithdrawal(@Param("uid") Long uid,
        @Param("isWithdrawal") Byte isWithdrawal, Pageable pageable);

    List<UserBook> findByUidAndIsWithdrawal(Long uid, Byte isWithdrawal);

    List<UserBook> findByBidAndIsWithdrawalOrderByTurnNo(Long bid, Byte isWithdrawal);

    Optional<UserBook> findByBidAndUidAndIsWithdrawal(Long bid, Long uid, Byte isWithdrawal);
}
