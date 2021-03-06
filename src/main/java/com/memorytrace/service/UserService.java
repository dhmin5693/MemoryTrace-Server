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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final BookService bookService;

    @Transactional
    public UserDetailResponseDto getExistingUser(UserSaveRequestDto request) {
        request.setSnsKey(request.getSnsType() + "_" + request.getSnsKey());
        User user = userRepository.findBySnsKey(request.getSnsKey()).orElse(null);
        if (user == null) {
            return null;
        }

        if (request.getToken() != null &&
            fcmTokenRepository.findByTokenAndUser_uid(request.getToken(), user.getUid()).isEmpty()) {
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
            log.error("?????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public UserDetailResponseDto findById(HttpHeaders headers) throws MethodArgumentNotValidException {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();

        User user = userRepository.findById(uid)
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(uid,
                    "?????? ????????? ????????????. uid =" + uid)));
        try {
            return new UserDetailResponseDto(user, headers.get("Authorization").get(0));
        } catch (Exception e) {
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public UserDetailResponseDto updateNickname(HttpHeaders headers, UserUpdateRequestDto request) throws MethodArgumentNotValidException {

            Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUid();
            User user = userRepository.findById(uid)
                .orElseThrow(() -> new MethodArgumentNotValidException(null,
                    new BeanPropertyBindingResult(uid,
                        "?????? ????????? ????????????. uid =" + uid)));
        try {
            user.updateNickname(request.getNickname());
            return new UserDetailResponseDto(user, headers.get("Authorization").get(0));
        } catch (Exception e) {
            log.error("????????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public Long withdraw() throws MethodArgumentNotValidException {
            Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUid();
            User user = userRepository.findById(uid)
                .orElseThrow(() -> new MethodArgumentNotValidException(null,
                    new BeanPropertyBindingResult(uid,
                        "?????? ????????? ????????????. uid =" + uid)));
        try {
            userBookRepository.findByUidAndIsWithdrawal(uid, (byte) 0).stream()
                .forEach(ub -> bookService.exitBook(ub.getBid()));
            user.withdraw();
            return uid;
        } catch (Exception e) {
            log.error("?????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void fcmSave(FcmSaveRequestDto request) {
        try {
            Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUid();
            if (request.getToken() != null && fcmTokenRepository
                .findByTokenAndUser_uid(request.getToken(), uid)
                .isEmpty()) {
                fcmTokenRepository.save(request.toEntity());
            }
        } catch (Exception e) {
            log.error("FCM ?????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public String getToken(Long uid) throws MethodArgumentNotValidException {
            User user = userRepository.findById(uid)
                .orElseThrow(() -> new MethodArgumentNotValidException(null,
                    new BeanPropertyBindingResult(uid,
                        "?????? ????????? ????????????. uid =" + uid)));
        try {
            return jwtTokenProvider.createToken(user.getSnsKey());
        } catch (Exception e) {
            log.error("?????? ?????? ??? ????????????", e);
            throw new MemoryTraceException();
        }
    }
}
