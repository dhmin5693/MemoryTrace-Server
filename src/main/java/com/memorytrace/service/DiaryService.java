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
import com.memorytrace.repository.CommentRepository;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserBookRepository userBookRepository;

    private final BookRepository bookRepository;

    private final FcmTokenRepository fcmTokenRepository;

    private final CommentRepository commentRepository;

    private final FirebaseMessagingService firebaseMessagingService;

    private final S3Uploder s3Uploder;

    @Transactional(readOnly = true)
    public DiaryListResponseDto findByBook_Bid(Long bid, PageRequestDto pageRequestDto)
        throws MethodArgumentNotValidException {
        Book book = bookRepository.findByBid(bid).orElseThrow(
            () -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(bid,
                    "?????? ?????? ???????????? ????????????. bid=" + bid)));
        try {
            Page<Diary> result = diaryRepository
                .findByBook_Bid(bid, pageRequestDto.getPageableWithSort(pageRequestDto));

            List<DiaryListResponseDto.DiaryList> diaryList = result.stream()
                .map(d -> new DiaryListResponseDto().new DiaryList(d))
                .collect(Collectors.toList());

            return new DiaryListResponseDto(result, book, diaryList);
        } catch (Exception e) {
            log.error("???????????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponseDto findByDid(Long did) throws MethodArgumentNotValidException {
        Diary entity = diaryRepository.findByDid(did)
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(did,
                    "?????? ??????????????? ????????????. did=" + did)));

        Long commentCnt = commentRepository.countCommentByDiary_did(did);

        if (LocalDateTime.now().isBefore(entity.getCreatedDate().plusMinutes(30))) {
            return new DiaryDetailResponseDto(entity, true, commentCnt);
        }

        return new DiaryDetailResponseDto(entity, false, commentCnt);
    }

    @Transactional
    public DiarySaveResponseDto save(DiarySaveRequestDto requestDto, MultipartFile file)
        throws IOException, MethodArgumentNotValidException {
        requestDto.setUid(((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid());

        userBookRepository
            .findByBidAndUidAndIsWithdrawal(requestDto.getBid(), requestDto.getUid(), (byte) 0)
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(requestDto.getBid(),
                    "?????? ??????????????? ???????????? ?????? ?????? ???????????????. bid=" + requestDto.getBid() + ", uid=" + requestDto
                        .getUid())));

        Book book = bookRepository.findByBidAndUser_Uid(requestDto.getBid(), requestDto.getUid())
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(requestDto.getBid(),
                    "?????? ???????????? ?????? ????????? ????????? ????????????. uid=" + requestDto.getUid())));

        String imgUrl = file == null ? null : s3Uploder.upload(file, "diary");
        try {
            User nextUser = getNextUser(requestDto.getBid(), requestDto.getUid());
            updateWhoseTurn(requestDto.getBid(), requestDto.getUid(), nextUser);

            Diary diary = diaryRepository.save(requestDto.toEntity(imgUrl));

            // ?????? ?????? ????????? ??????????????? ??????
            firebaseMessagingService.sendMulticast(
                Message.builder().subject(book.getTitle())
                    .content("????????? ????????? ??????????????????!")
                    .data(book).build(),
                fcmTokenRepository.findTokenBidAndUidNotInMe(book.getBid(), requestDto.getUid()));

            List<String> allTokens = fcmTokenRepository.findByUser_Uid(nextUser.getUid()).stream()
                .map(fcmToken -> fcmToken.getToken())
                .collect(Collectors.toList());

            // ?????? ????????? ???????????? ??????
            if (!requestDto.getUid().equals(nextUser.getUid())) {
                firebaseMessagingService.sendMulticast(
                    Message.builder().subject(book.getTitle())
                        .content(nextUser.getNickname() + "?????? ?????? ?????? ????????? ???????????????!")
                        .data(book)
                        .build(), allTokens);
            }

            return new DiarySaveResponseDto(diary);
        } catch (Exception e) {
            log.error("???????????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public User getNextUser(Long bid, Long uid) throws MethodArgumentNotValidException {
        int index = 0;
        List<UserBook> userBookList = Optional
            .ofNullable(userBookRepository.findByBidAndIsWithdrawalOrderByTurnNo(bid, (byte) 0))
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(bid,
                    "?????? ?????? UserBook??? ????????????. bid=" + bid)));

        try {
            for (int i = 0; i < userBookList.size(); i++) {
                UserBook userBook = userBookList.get(i);
                if (userBook.getUid().equals(uid)) {
                    index = i == userBookList.size() - 1 ? 0 : i + 1;
                    break;
                }
            }

            return userBookList.get(index).getUser();
        } catch (Exception e) {
            log.error("Whose Turn ?????? ??? ?????? ??????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void updateWhoseTurn(Long bid, Long currentUid, User nextUser) throws MethodArgumentNotValidException {
        Book book = bookRepository.findByBid(bid).orElseThrow(
            () -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(bid,
                    "?????? ?????? ???????????? ????????????. bid=" + bid)));

        if (nextUser.getUid().equals(currentUid)) {
            book.updateModifiedDate();
        } else {
            book.updateWhoseTurnBook(bid, nextUser);
        }
    }

    @Transactional
    public void updateDiary(DiaryUpdateRequestDto request, MultipartFile file) throws MethodArgumentNotValidException {
        Diary diary = diaryRepository.findByDid(request.getDid()).orElseThrow(
            () -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(request.getDid(),
                    "?????? ?????? ??????????????? ????????????. did=" + request.getDid())));

        try {
            String imgUrl = file == null ? diary.getImg() : s3Uploder.upload(file, "diary");

            diary.update(request.getTitle(), imgUrl, request.getContent());
        } catch (Exception e) {
            log.error("Diary ?????? ??? ?????? ??????", e);
            throw new MemoryTraceException();
        }
    }
}
