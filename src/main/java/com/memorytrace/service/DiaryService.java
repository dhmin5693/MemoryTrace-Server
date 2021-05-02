package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.DiarySaveRequestDto;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.dto.response.DiarySaveResponseDto;
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
    public DiaryListResponseDto findByBook_BidOrderByModifiedDateDesc(Long bid) {
        Long whoseTurn = bookRepository.findByBid(bid).getUser().getUid();
        List<DiaryListResponseDto.DiaryList> diaryList = diaryRepository
            .findByBook_BidOrderByModifiedDateDesc(bid).stream()
            .map(d -> new DiaryListResponseDto().new DiaryList(d))
            .collect(Collectors.toList());
        return new DiaryListResponseDto(whoseTurn, diaryList);
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponseDto findByDid(Long did) {
        Diary entity = diaryRepository.findByDid(did)
            .orElseThrow(() -> new IllegalArgumentException(("해당 다이어리가 없습니다. did=" + did)));
        return new DiaryDetailResponseDto(entity);
    }

    @Transactional
    public DiarySaveResponseDto save(DiarySaveRequestDto requestDto, MultipartFile file)
        throws IOException {
        String imgUrl = s3Uploder.upload(file, "diary");
        updateWhoseTurnNo(requestDto.getBid(), requestDto.getUid());
        Diary diary = diaryRepository.save(requestDto.toEntity(imgUrl));
        return new DiarySaveResponseDto(diary);
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
