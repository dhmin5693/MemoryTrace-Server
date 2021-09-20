package com.memorytrace.service;

import com.memorytrace.domain.Comment;
import com.memorytrace.domain.User;
import com.memorytrace.dto.request.CommentSaveRequestDto;
import com.memorytrace.dto.response.CommentSaveResponseDto;
import com.memorytrace.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentSaveResponseDto save(CommentSaveRequestDto requestDto) {
        requestDto.setUid(((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid());

        // TODO: 해당 일기장에 참여 중인 유저인지 체크

        Comment comment = commentRepository.save(requestDto.toEntity());

        return new CommentSaveResponseDto(comment.getCid());
    }
}
