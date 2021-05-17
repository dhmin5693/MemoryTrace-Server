package com.memorytrace.service;

import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.User;
import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.dto.response.UserDetailResponseDto;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.UserRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final S3Uploder s3Uploder;

    @Transactional
    public UserDetailResponseDto save(UserSaveRequestDto request, MultipartFile file)
        throws IOException {
        String imgUrl = file == null ? null : s3Uploder.upload(file, "profile");

        try {
            User entity = userRepository.save(request.toEntity(imgUrl));

            return new UserDetailResponseDto(entity);
        } catch (Exception e) {
            log.error("유저 저장 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }
}
