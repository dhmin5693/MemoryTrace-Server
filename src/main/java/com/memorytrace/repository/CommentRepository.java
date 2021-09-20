package com.memorytrace.repository;

import com.memorytrace.domain.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCidAndUser_Uid(Long cid, Long uid);

    Long countCommentByDiary_did(Long did);
}
