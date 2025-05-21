package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityCommentEntity;
import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentRequestDto {
    private Integer postId;
    private Integer userId;
    private String commentContent;

    public CommunityCommentEntity toEntity(CommunityPostEntity postEntity, UserEntity userEntity) {
        return CommunityCommentEntity.builder()
                .postEntity(postEntity)
                .userEntity(userEntity)
                .commentContent(commentContent)
                .build();
    }
}

