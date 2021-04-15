package com.memorytrace.repository;

import com.memorytrace.domain.Diary;
import com.memorytrace.dto.response.DiaryListResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<DiaryListResponseDto> findByBook_BidOrderByModifiedDateDesc(Long bid);

    Optional<Diary> findByDid(Long did);
}
