package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
import com.memorytrace.domain.User;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.DiarySaveRequestDto;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.DiaryRepository;
import com.memorytrace.repository.UserBookRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserBookRepository userBookRepository;

    private final BookRepository bookRepository;

    private final S3Uploder s3Uploder;

    @Transactional(readOnly = true)
    public List<DiaryListResponseDto> findByBook_BidOrderByModifiedDateDesc(Long bid) {
        return diaryRepository.findByBook_BidOrderByModifiedDateDesc(bid).stream()
            .map(DiaryListResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponseDto findByDid(Long did) {
        Diary entity = diaryRepository.findByDid(did)
            .orElseThrow(() -> new IllegalArgumentException(("해당 다이어리가 없습니다. did=" + did)));
        return new DiaryDetailResponseDto(entity);
    }

    @Transactional
    public void save(DiarySaveRequestDto requestDto, MultipartFile file) throws IOException {
        String imgUrl = s3Uploder.upload(file, "diary");
        updateWhoseTurnNo(requestDto.getBid(), requestDto.getUid());
        diaryRepository.save(requestDto.toEntity(imgUrl));
    }

    @Transactional
    public void updateWhoseTurnNo(Long bid, Long uid) {
        int index = 0;
        List<UserBook> userBookList = Optional
            .ofNullable(userBookRepository.findByBidAndIsWithdrawal(bid, (byte) 0))
            .orElseThrow(() -> new IllegalArgumentException("검색 되는 UserBook이 없습니다. bid=" + bid));

        for (UserBook userBook : userBookList) {
            if (userBook.getUid() == uid) {
                index =
                    userBook.getTurnNo() == userBookList.size() - 1 ? 0 : userBook.getTurnNo() + 1;
                break;
            }
        }

        Book book = Optional.ofNullable(bookRepository.findByBid(bid))
            .orElseThrow(() -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + bid));

        bookRepository.save(
            book.updateWhoseTurnBook(bid, userBookList.get(index).getUid())
        );
    }
}
