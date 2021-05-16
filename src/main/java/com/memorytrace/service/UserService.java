package com.memorytrace.service;

import com.memorytrace.auth.JwtTokenProvider;
import com.memorytrace.common.S3Uploder;
import com.memorytrace.domain.User;
import com.memorytrace.dto.request.UserSaveRequestDto;
import com.memorytrace.dto.response.UserDetailResponseDto;
import com.memorytrace.exception.InternalServerException;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final S3Uploder s3Uploder;

    @Transactional
    public UserDetailResponseDto save(UserSaveRequestDto request, MultipartFile file)
        throws IOException {
        String imgUrl = file == null ? null : s3Uploder.upload(file, "profile");

        try {
            request.setSnsKey(request.getSnsType() + "_" + request.getSnsKey());
            User entity = userRepository.save(request.toEntity(imgUrl));
            String jwt = jwtTokenProvider.createToken(request.getSnsKey());
            return new UserDetailResponseDto(entity, jwt);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Transactional(readOnly = true)
    public String getToken(Long uid) {
        User user = userRepository.findByUid(uid).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 uid입니다. uid=" + uid));
        return jwtTokenProvider.createToken(user.getSnsKey());
    }
}
