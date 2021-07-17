package com.memorytrace.service;

import com.memorytrace.domain.FcmToken;
import com.memorytrace.domain.User;
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

    public void deletFcmToken(String token) {
        Long uid = ((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid();
        List<FcmToken> fcmTokenList = fcmTokenRepository.findByTokenAndUser_uid(token, uid);
        fcmTokenRepository.deleteAll(fcmTokenList);
    }
}
