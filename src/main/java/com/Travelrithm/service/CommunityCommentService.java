package com.Travelrithm.service;

import com.Travelrithm.domain.CommunityCommentEntity;
import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.dto.CommunityCommentRequestDto;
import com.Travelrithm.dto.CommunityCommentResponseDto;
import com.Travelrithm.repository.CommunityCommentRepository;
import com.Travelrithm.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityPostRepository postRepository;

    // 댓글 생성
    public CommunityCommentResponseDto createComment(CommunityCommentRequestDto request) {
        CommunityPostEntity postEntity = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        CommunityCommentEntity entity = request.toEntity(postEntity);
        return new CommunityCommentResponseDto(commentRepository.save(entity));
    }

    // 댓글 조회
    public List<CommunityCommentResponseDto> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostEntity_PostId(postId)
                .stream()
                .map(CommunityCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    public CommunityCommentResponseDto updateComment(Integer commentId, CommunityCommentRequestDto request) {
        CommunityCommentEntity entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        entity.update(request.getCommentContent());
        return new CommunityCommentResponseDto(entity);
    }

    // 댓글 삭제
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}

