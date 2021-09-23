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

    // FEEDBACK 넘겨줄 땐 Map보다는 UserBook 처럼 정의된 타입으로 넘겨주는게 좋습니다.
    // https://stackoverflow.com/questions/13012584/jpa-how-to-convert-a-native-query-result-set-to-pojo-class-collection/21487061#21487061
    // isWithdrawal 는 byte를 사용하고 있는데 boolean으로 바꾸는게 가독성 면에서 더 좋아보여요.
    // https://memostack.tistory.com/194
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
