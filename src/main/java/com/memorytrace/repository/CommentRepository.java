package com.memorytrace.repository;

import com.memorytrace.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * "
        + "from comment "
        + "where did = :did "
        + "ORDER BY COALESCE(parent, cid), parent IS NOT NULL, cid"
        , nativeQuery = true)
    List<Comment> findByDid(@Param("did") Long did);
}
