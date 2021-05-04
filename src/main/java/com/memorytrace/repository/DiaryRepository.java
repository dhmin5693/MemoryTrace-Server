package com.memorytrace.repository;

import com.memorytrace.domain.Diary;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findByBook_Bid(Long bid, Pageable pageable);

    Optional<Diary> findByDid(Long did);
}
