package com.memorytrace.service;

import com.memorytrace.domain.User;
import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.dto.response.UserDetailResponseDto;
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
    public UserDetailResponseDto save(UserSaveRequestDto request) {
        User entity = userRepository.save(request.toEntity());
        return new UserDetailResponseDto(entity);
    }
}
