package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.Book;
import com.memorytrace.domain.Diary;
import com.memorytrace.domain.User;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.DiarySaveRequestDto;
import com.memorytrace.dto.request.DiaryUpdateRequestDto;
import com.memorytrace.dto.request.Message;
import com.memorytrace.dto.request.PageRequestDto;
import com.memorytrace.dto.response.DiaryDetailResponseDto;
import com.memorytrace.dto.response.DiaryListResponseDto;
import com.memorytrace.dto.response.DiarySaveResponseDto;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.DiaryRepository;
import com.memorytrace.repository.FcmTokenRepository;
import com.memorytrace.repository.UserBookRepository;
import java.io.IOException;
import java.time.LocalDateTime;
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

    private final FcmTokenRepository fcmTokenRepository;

    private final FirebaseMessagingService firebaseMessagingService;

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

        if (LocalDateTime.now().isBefore(entity.getCreatedDate().plusMinutes(30))) {
            return new DiaryDetailResponseDto(entity, true);
        }

        return new DiaryDetailResponseDto(entity, false);
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

        Book book = bookRepository.findByBidAndUser_Uid(requestDto.getBid(), requestDto.getUid())
            .orElseThrow(() -> new IllegalArgumentException(
                "현재 교환일기 작성 차례인 유저가 아닙니다. uid=" + requestDto.getUid()));
        String imgUrl = file == null ? null : s3Uploder.upload(file, "diary");
        try {
            User nextUser = updateWhoseTurnNo(requestDto.getBid(), requestDto.getUid());

            Diary diary = diaryRepository.save(requestDto.toEntity(imgUrl));

            // 자신 제외 나머지 사람들에게 알람
            firebaseMessagingService.sendMulticast(
                Message.builder().subject(book.getTitle())
                    .content("새로운 글이 등록되었어요!")
                    .data(book).build(),
                fcmTokenRepository.findTokenBidAndUidNotInMe(book.getBid(), requestDto.getUid()));

            List<String> allTokens = fcmTokenRepository.findByUser_Uid(nextUser.getUid()).stream()
                .map(fcmToken -> fcmToken.getToken())
                .collect(Collectors.toList());

            // 자기 차례인 사람에게 알람
            if (requestDto.getUid() != nextUser.getUid()) {
                firebaseMessagingService.sendMulticast(
                    Message.builder().subject(book.getTitle())
                        .content(nextUser.getNickname() + "님의 일기 작성 차례가 돌아왔어요!")
                        .data(book)
                        .build(), allTokens);
            }

            return new DiarySaveResponseDto(diary);
        } catch (Exception e) {
            log.error("교환일기 저장 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public User updateWhoseTurnNo(Long bid, Long uid) {
        int index = 0;
        List<UserBook> userBookList = Optional
            .ofNullable(userBookRepository.findByBidAndIsWithdrawalOrderByTurnNo(bid, (byte) 0))
            .orElseThrow(() -> new IllegalArgumentException("검색 되는 UserBook이 없습니다. bid=" + bid));

        try {
            for (int i = 0; i < userBookList.size(); i++) {
                UserBook userBook = userBookList.get(i);
                if (userBook.getUid().equals(uid)) {
                    index = i == userBookList.size() - 1 ? 0 : i + 1;
                    break;
                }
            }

            Book book = bookRepository.findByBid(bid).orElseThrow(
                () -> new IllegalArgumentException("검색 되는 책이 없습니다. bid=" + bid));

            book.updateWhoseTurnBook(bid, userBookList.get(index).getUser());

            return userBookList.get(index).getUser();
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

            String imgUrl = file == null ? diary.getImg() : s3Uploder.upload(file, "diary");

            diary.update(request.getTitle(), imgUrl, request.getContent());
        } catch (Exception e) {
            log.error("Diary 수정 중 에러 발생", e);
            throw new MemoryTraceException();
        }
    }
}
