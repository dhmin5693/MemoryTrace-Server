package com.memorytrace.service;

import com.memorytrace.domain.Comment;
import com.memorytrace.domain.Diary;
import com.memorytrace.domain.User;
import com.memorytrace.dto.request.CommentSaveRequestDto;
import com.memorytrace.dto.request.Message;
import com.memorytrace.dto.response.CommentListResponseDto;
import com.memorytrace.dto.response.CommentSaveResponseDto;
import com.memorytrace.exception.MemoryTraceException;
import com.memorytrace.repository.CommentRepository;
import com.memorytrace.repository.DiaryRepository;
import com.memorytrace.repository.FcmTokenRepository;
import com.memorytrace.repository.UserBookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserBookRepository userBookRepository;

    private final DiaryRepository diaryRepository;

    private final FcmTokenRepository fcmTokenRepository;

    private final FirebaseMessagingService firebaseMessagingService;

    @Transactional
    public CommentSaveResponseDto save(CommentSaveRequestDto requestDto)
        throws MethodArgumentNotValidException {
        requestDto.setUid(((User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal()).getUid());

        Diary diary = diaryRepository.findByDid(requestDto.getDid())
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(requestDto.getDid(),
                    "해당 다이어리가 없습니다. did=" + requestDto.getDid())));

        userBookRepository
            .findByBidAndUidAndIsWithdrawal(diary.getBook().getBid(), requestDto.getUid(), (byte) 0)
            .orElseThrow(() -> new MethodArgumentNotValidException(null,
                new BeanPropertyBindingResult(diary.getBook().getBid(),
                    "해당 교환일기에 참여하고 있지 않은 유저입니다. bid=" + diary.getBook().getBid() + ", uid="
                        + requestDto
                        .getUid())));

        try {
            Comment comment = commentRepository.save(requestDto.toEntity());

            // 다이어리 작성자와 댓글 작성자가 같으면 알림 X
            if (!diary.getUser().getUid().equals(requestDto.getUid())) {
                List<String> diaryWriterToken = fcmTokenRepository
                    .findByUser_Uid(diary.getUser().getUid()).stream()
                    .map(fcmToken -> fcmToken.getToken())
                    .collect(Collectors.toList());

                firebaseMessagingService.sendMulticast(
                    Message.builder().subject("[댓글]")
                        .content(requestDto.getContent())
                        .data(null).build(), diaryWriterToken);
            }

            // TODO: 대댓글: 다이어리 작성자 + 본 댓 작성자 알림
            if (requestDto.getParent() != 0) {
                Comment originComment = commentRepository.findByCid(requestDto.getParent());

                if (!originComment.getUser().getUid().equals(requestDto.getUid())) {
                    List<String> originCommentWriterToken = fcmTokenRepository
                        .findByUser_Uid(originComment.getUser().getUid()).stream()
                        .map(fcmToken -> fcmToken.getToken())
                        .collect(Collectors.toList());

                    firebaseMessagingService.sendMulticast(
                        Message.builder().subject("[답글]")
                            .content(requestDto.getContent())
                            .data(null).build(), originCommentWriterToken);
                }
            }

            return new CommentSaveResponseDto(comment.getCid());
        } catch (Exception e) {
            log.error("댓글 작성 중 에러발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional
    public void deleteComment(Long cid) throws MethodArgumentNotValidException {
        Comment comment = commentRepository
            .findByCidAndUser_Uid(cid,
                ((User) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal()).getUid()).orElseThrow(
                () -> new MethodArgumentNotValidException(null,
                    new BeanPropertyBindingResult(cid, "검색 되는 댓글이 없습니다. cid = " + cid))
            );

        try {
            comment.delete();
        } catch (Exception e) {
            log.error("댓글 삭제 중 에러 발생", e);
            throw new MemoryTraceException();
        }
    }

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> findByDid(Long did) {
        List<Comment> commentList = commentRepository.findByDid(did);
        List<CommentListResponseDto> dtoList = new ArrayList<>();
        CommentListResponseDto dto = null;
        for (Comment c : commentList) {
            if (c.getParent() == null) {
                dtoList.add(new CommentListResponseDto(c, new ArrayList<>()));
                dto = dtoList.get(dtoList.size() - 1);
            } else {
                dto.getCommentList().add(new CommentListResponseDto().new CommentList(c));
            }
        }
        return dtoList;
    }
}
