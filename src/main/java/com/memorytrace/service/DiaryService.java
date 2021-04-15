package com.memorytrace.service;

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
}
