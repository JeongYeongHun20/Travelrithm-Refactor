package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityComment;
import com.Travelrithm.domain.CommunityPost;
import com.Travelrithm.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentRequestDto {
    private Integer postId;
    private Long userId;
    private String commentContent;

    public CommunityComment toEntity(CommunityPost postEntity, Member member) {
        return CommunityComment.builder()
                .communityPost(postEntity)
                .member(member)
                .commentContent(commentContent)
                .build();
    }
}

