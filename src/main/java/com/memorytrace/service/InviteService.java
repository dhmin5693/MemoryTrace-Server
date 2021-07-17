package com.memorytrace.service;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.User;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.InviteSaveRequestDto;
import com.memorytrace.dto.request.Message;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.FcmTokenRepository;
import com.memorytrace.repository.UserBookRepository;
import com.memorytrace.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InviteService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final UserBookRepository userBookRepository;

    private final FcmTokenRepository fcmTokenRepository;

    private final FirebaseMessagingService firebaseMessagingService;

    @Transactional
    public void save(InviteSaveRequestDto request) {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();

        User user = userRepository.findByUid(uid)
            .orElseThrow(() -> new IllegalArgumentException(
                "유효하지 않는 사용자 입니다. uid = " + uid));

        Book book = Optional.ofNullable(bookRepository.findByInviteCode(request.getInviteCode()))
            .orElseThrow(() -> new IllegalArgumentException(
                "유효하지 않는 초대코드 입니다. 다시 시도해주세요."));

        if (userBookRepository.findByBidAndUidAndIsWithdrawal(book.getBid(), uid, (byte) 0)
            .isPresent()) {
            throw new IllegalArgumentException("해당 교환일기에 이미 참여하고 있는 중입니다. ");
        }

        try {
            List<UserBook> userBook = userBookRepository
                .findByBidAndIsWithdrawalOrderByTurnNo(book.getBid(), (byte) 0);

            final int nowTurn = userBook.size();

            userBookRepository.save(UserBook.builder()
                .bid(book.getBid())
                .uid(uid)
                .isWithdrawal((byte) 0)
                .turnNo(nowTurn)
                .build());

            book.updateModifiedDate();

            // TODO: 자신 제외 나머지 사람들에게 초대사람 완료 알림
            firebaseMessagingService.sendMulticast(
                Message.builder().subject(book.getTitle())
                    .content("새로운 멤버 "+ user.getNickname() + "님을 환영해주세요! ")
                    .data(book).build(),
                fcmTokenRepository.findTokenBidAndUidNotInMe(book.getBid(), uid));
        } catch (Exception e) {
            log.error("초대한 멤버 저장 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }
}
