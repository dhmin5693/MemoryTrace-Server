package com.memorytrace.repository;

import com.memorytrace.domain.FcmToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    List<FcmToken> findByUser_Uid(Long nextTurnUid);

    @Query(value = "select token "
        + "from user_book ub, fcm_token f "
        + "where ub.uid = f.uid "
        + "and ub.is_withdrawal = 0 "
        + "and bid = :bid "
        + "and f.uid NOT IN (:uid)", nativeQuery = true)
    List<String> findTokenBidAndUidNotInMe(@Param("bid") Long bid, @Param("uid") Long uid);

    List<FcmToken> findByTokenAndUser_uid(String token, Long uid);
}
