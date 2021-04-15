package com.memorytrace.service;

import com.memorytrace.domain.Diary;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.repository.DiaryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Transactional(readOnly = true)
    public List<DiaryListResponseDto> findByBook_BidOrderByModifiedDateDesc(Long bid) {
        return diaryRepository.findByBook_BidOrderByModifiedDateDesc(bid);
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponseDto findByDid(Long did) {
        Diary entity = diaryRepository.findByDid(did)
            .orElseThrow(() -> new IllegalArgumentException(("해당 다이어리가 없습니다. did=" + did)));
        return new DiaryDetailResponseDto(entity);
    }
}
