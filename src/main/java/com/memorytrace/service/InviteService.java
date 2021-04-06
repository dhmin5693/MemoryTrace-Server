package com.memorytrace.service;

import com.memorytrace.domain.Book;
import com.memorytrace.domain.UserBook;
import com.memorytrace.dto.request.InviteSaveRequestDto;
import com.memorytrace.repository.BookRepository;
import com.memorytrace.repository.UserBookRepository;
import com.memorytrace.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;

    @Transactional
    public void save(InviteSaveRequestDto request) {
        findbyUid(request.getUid());
        Long bid = findByInviteCode(request.getInviteCode()).getBid();
        List<UserBook> userBook = userBookRepository.findByBidAndIsWithdrawal(bid, (byte) 0);

        final int nowTurn = userBook.size();

        userBookRepository.save(UserBook.builder()
            .bid(bid)
            .uid(request.getUid())
            .isWithdrawal((byte) 0)
            .turnNo(nowTurn)
            .build());
    }

    @Transactional(readOnly = true)
    public Book findByInviteCode(String inviteCode) {
        return Optional.ofNullable(bookRepository.findByInviteCode(inviteCode))
            .orElseThrow(() -> new IllegalArgumentException(
                "유효하지 않는 초대코드 입니다. 다시 시도해주세요."));
    }

    @Transactional(readOnly = true)
    public void findbyUid(Long uid) {
        Optional.ofNullable(userRepository.findByUid(uid))
            .orElseThrow(() -> new IllegalArgumentException(
                "유효하지 않는 사용자 입니다. uid = " + uid));
    }
}
