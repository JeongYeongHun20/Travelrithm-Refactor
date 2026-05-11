package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityComment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentResponseDto {
    private Integer commentId;
    private Integer postId;
    private Long userId;
    private String nickname;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommunityCommentResponseDto(CommunityComment entity) {
        this.commentId = entity.getCommentId();
        this.postId = entity.getCommunityPost().getCommunityPostId();
        this.userId = entity.getMember().getMemberId();
        this.nickname = entity.getMember() != null ? entity.getMember().getNickname() : null;
        this.commentContent = entity.getCommentContent();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}

