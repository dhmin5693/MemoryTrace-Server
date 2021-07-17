package com.memorytrace.service;

import com.memorytrace.auth.JwtTokenProvider;
import com.memorytrace.domain.FcmToken;
import com.memorytrace.domain.User;
import com.memorytrace.dto.request.FcmSaveRequestDto;
import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.dto.request.UserUpdateRequestDto;
import com.memorytrace.dto.response.UserDetailResponseDto;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.FcmTokenRepository;
import com.memorytrace.repository.UserBookRepository;
import com.memorytrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final BookService bookService;

    @Transactional(readOnly = true)
    public UserDetailResponseDto getExistingUser(UserSaveRequestDto request) {
        request.setSnsKey(request.getSnsType() + "_" + request.getSnsKey());
        User user = userRepository.findBySnsKey(request.getSnsKey()).orElse(null);
        if (user == null) {
            return null;
        }
        if (request.getToken() != null &&
            fcmTokenRepository.findByTokenAndUser_uid(request.getToken(), user.getUid())
                .isEmpty()) {
            fcmTokenRepository
                .save(FcmToken.builder().user(user).token(request.getToken()).build());
        }
        String jwt = jwtTokenProvider.createToken(request.getSnsKey());
        return new UserDetailResponseDto(user, jwt);
    }

    @Transactional
    public UserDetailResponseDto save(UserSaveRequestDto request) {
        try {
            String jwt = jwtTokenProvider.createToken(request.getSnsKey());
            User user = userRepository.save(request.toEntity());
            if (request.getToken() != null) {
                fcmTokenRepository.save(FcmToken.builder()
                    .user(user)
                    .token(request.getToken())
                    .build());
            }
            return new UserDetailResponseDto(user, jwt);
        } catch (Exception e) {
            log.error("유저 저장 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public UserDetailResponseDto findById(HttpHeaders headers) {
        try {
            Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUid();
            User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. uid=" + uid));
            return new UserDetailResponseDto(user, headers.get("Authorization").get(0));
        } catch (Exception e) {
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public UserDetailResponseDto updateNickname(HttpHeaders headers, UserUpdateRequestDto request) {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();
        User user = userRepository.findById(uid)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. uid=" + uid));
        user.updateNickname(request.getNickname());
        return new UserDetailResponseDto(user, headers.get("Authorization").get(0));
    }

    @Transactional
    public Long withdraw() {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();
        User user = userRepository.findById(uid)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. uid=" + uid));
        userBookRepository.findByUidAndIsWithdrawal(uid, (byte) 0).stream()
            .forEach(ub -> bookService.exitBook(ub.getBid()));
        user.withdraw();
        return uid;
    }

    @Transactional
    public void fcmSave(FcmSaveRequestDto request) {
        if (request.getToken() != null) {
            fcmTokenRepository.save(request.toEntity());
        }
    }

    @Transactional(readOnly = true)
    public String getToken(Long uid) {
        User user = userRepository.findByUid(uid).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 uid입니다. uid=" + uid));
        return jwtTokenProvider.createToken(user.getSnsKey());
    }
}
