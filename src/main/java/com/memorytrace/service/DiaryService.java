package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
import com.memorytrace.domain.User;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.DiarySaveRequestDto;
import com.memorytrace.dto.request.DiaryUpdateRequestDto;
import com.memorytrace.dto.request.PageRequestDto;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.dto.response.DiarySaveResponseDto;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.DiaryRepository;
import com.memorytrace.repository.UserBookRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserBookRepository userBookRepository;

    private final BookRepository bookRepository;

    private final S3Uploder s3Uploder;

    @Transactional(readOnly = true)
    public DiaryListResponseDto findByBook_Bid(Long bid, PageRequestDto pageRequestDto) {
        try {
            Book book = bookRepository.findByBid(bid).orElseThrow(
                () -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + bid));

            Page<Diary> result = diaryRepository
                .findByBook_Bid(bid, pageRequestDto.getPageableWithSort(pageRequestDto));

            List<DiaryListResponseDto.DiaryList> diaryList = result.stream()
                .map(d -> new DiaryListResponseDto().new DiaryList(d))
                .collect(Collectors.toList());

            return new DiaryListResponseDto(result, book, diaryList);
        } catch (Exception e) {
            log.error("교환일기 조회 중 에러발생", e);
            throw new MemoryTraceException();
        }
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
        requestDto.setUid(((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid());
        userBookRepository
            .findByBidAndUidAndIsWithdrawal(requestDto.getBid(), requestDto.getUid(), (byte) 0)
            .orElseThrow(() -> new IllegalArgumentException("해당 교환일기에 참여하고 있지 않은 유저입니다. "
                + "bid=" + requestDto.getBid() + ", uid=" + requestDto.getUid()));

        bookRepository.findByBidAndUser_Uid(requestDto.getBid(), requestDto.getUid())
            .orElseThrow(() -> new IllegalArgumentException(
                "현재 교환일기 작성 차례인 유저가 아닙니다. uid=" + requestDto.getUid()));
        String imgUrl = file == null ? null : s3Uploder.upload(file, "diary");
        try {
            updateWhoseTurnNo(requestDto.getBid(), requestDto.getUid());

            Diary diary = diaryRepository.save(requestDto.toEntity(imgUrl));

            return new DiarySaveResponseDto(diary);
        } catch (Exception e) {
            log.error("교환일기 저장 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void updateWhoseTurnNo(Long bid, Long uid) {
        int index = 0;
        List<UserBook> userBookList = Optional
            .ofNullable(userBookRepository.findByBidAndIsWithdrawal(bid, (byte) 0))
            .orElseThrow(() -> new IllegalArgumentException("검색 되는 UserBook이 없습니다. bid=" + bid));

        try {
            for (UserBook userBook : userBookList) {
                if (userBook.getUid() == uid) {
                    index =
                        userBook.getTurnNo() == userBookList.size() - 1 ? 0
                            : userBook.getTurnNo() + 1;
                    break;
                }
            }

            Book book = bookRepository.findByBid(bid).orElseThrow(
                () -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + bid));

            book.updateWhoseTurnBook(bid, userBookList.get(index).getUser());
        } catch (Exception e) {
            log.error("Whose Turn 수정 중 에러 발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void updateDiary(DiaryUpdateRequestDto request, MultipartFile file) {
        try {
            Diary diary = diaryRepository.findByDid(request.getDid()).orElseThrow(
                () -> new IllegalArgumentException("검색 되는 다이어리가 없습니다. did=" + request.getDid()));

            String imgUrl = file == null ? request.getExistingImg() : s3Uploder.upload(file, "diary");

            diary.update(request.getTitle(), imgUrl, request.getContent());
        } catch (Exception e) {
            log.error("Diary 수정 중 에러 발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void exitDiary(Long bid) {
        try {
            Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal())
                .getUid();

            Optional<Book> book = bookRepository.findByBidAndUser_Uid(bid, uid);

            List<UserBook> userBookList = userBookRepository
                .findByBidAndIsWithdrawal(bid, (byte) 0);

            int idx = userBookList.stream().map(d -> d.getUid())
                .collect(Collectors.toList()).indexOf(uid);

            if (book.isPresent()) {
                if (userBookList.size() == 1) {
                    book.get().delete();
                } else {
                    User nextUser = idx == userBookList.size() - 1
                        ? userBookList.get(0).getUser() : userBookList.get(idx + 1).getUser();
                    book.get().updateWhoseTurnBook(bid, nextUser);
                }
            }

            userBookList.get(idx).exit();
        } catch (Exception e) {
            log.error("다이어리 나가기 중 에러 발생", e);
            throw new MemoryTraceException();
        }
    }
}
