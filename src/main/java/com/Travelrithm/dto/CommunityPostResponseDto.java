package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityPostEntity;
import lombok.*;

import java.time.LocalDateTime;


public record CommunityPostResponseDto (
        Integer postId,
        Integer userId,
        String title,
        String postContent,
        Boolean isTravelPlan,
        Integer planId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String regionThumbnailUrl
        ){

    public CommunityPostResponseDto(CommunityPostEntity postEntity) {
        this(
                postEntity.getPostId(),
                postEntity.getUserEntity().getUserId(),
                postEntity.getTitle(),
                postEntity.getPostContent(),
                postEntity.getIsTravelPlan(),
                postEntity.getPlanEntity()!= null ? postEntity.getPlanEntity().getPlanId() : null,

                postEntity.getCreatedAt(),
                postEntity.getUpdatedAt(),
                postEntity.getPlanEntity() != null && postEntity.getPlanEntity().getRegionEntity() != null
                        ? postEntity.getPlanEntity().getRegionEntity().getThumbnailImageUrl()
                        : null
        );
    }
}
