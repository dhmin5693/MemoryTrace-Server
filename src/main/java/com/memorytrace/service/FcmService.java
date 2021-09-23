package com.memorytrace.service;

import com.memorytrace.domain.FcmToken;
import com.memorytrace.domain.User;
import com.memorytrace.dto.request.FcmDeleteRequestDto;
import com.memorytrace.repository.FcmTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {
    private final FcmTokenRepository fcmTokenRepository;

    // FEADBACK 동작 중 오류가 발생하면 transactional 이 없어서 데이터 불일치가 발생합니다.
    public void deleteByFcmToken(FcmDeleteRequestDto requestDto) {
        List<FcmToken> fcmTokenList = fcmTokenRepository.findByTokenAndUser_uid(requestDto.getToken(), requestDto.getUid());
        fcmTokenRepository.deleteAll(fcmTokenList);
    }

    public void deleteAllTokens() {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();
        List<FcmToken> fcmTokenList = fcmTokenRepository.findByUser_uid(uid);
        fcmTokenRepository.deleteAll(fcmTokenList);
    }
}
