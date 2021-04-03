package com.memorytrace.service;

import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(UserSaveRequestDto request) {
        userRepository.save(request.toEntity());
    }
}
