package com.Travelrithm.service;

import com.Travelrithm.domain.CommunityCommentEntity;
import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.UserEntity;
import com.Travelrithm.dto.CommunityCommentRequestDto;
import com.Travelrithm.dto.CommunityCommentResponseDto;
import com.Travelrithm.repository.CommunityCommentRepository;
import com.Travelrithm.repository.CommunityPostRepository;
import com.Travelrithm.repository.UserRepository;
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
    private final UserRepository userRepository;


    public CommunityCommentResponseDto createComment(CommunityCommentRequestDto request) {
        CommunityPostEntity postEntity = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        UserEntity userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        CommunityCommentEntity entity = request.toEntity(postEntity, userEntity);
        return new CommunityCommentResponseDto(commentRepository.save(entity));
    }


    public List<CommunityCommentResponseDto> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostEntity_PostId(postId)
                .stream()
                .map(CommunityCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public CommunityCommentResponseDto updateComment(Integer commentId, CommunityCommentRequestDto request) {
        CommunityCommentEntity entity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        entity.update(request.getCommentContent());
        return new CommunityCommentResponseDto(entity);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}

