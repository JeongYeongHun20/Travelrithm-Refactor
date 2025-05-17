package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityPostEntity;
import com.Travelrithm.domain.PlanEntity;

import java.time.LocalDateTime;
import java.util.List;

public record CommunityPostResponseDto(
        Integer postId,
        Integer userId,
        String title,
        String postContent,
        Boolean isTravelPlan,
        Integer planId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String regionThumbnailUrl,
        String nickname,
        PlanResponseDto plan,
        List<PlaceDto> places,
        PlanResponseDto popularPlan
) {
    /**
     * 정적 팩토리 메서드: CommunityPostEntity → CommunityPostResponseDto 변환
     */
    public static CommunityPostResponseDto fromEntity(CommunityPostEntity post) {
        PlanEntity plan = post.getPlanEntity();

        String regionThumbnail = (plan != null && plan.getRegionEntity() != null)
                ? plan.getRegionEntity().getThumbnailImageUrl()
                : null;

        PlanResponseDto planDto = (plan != null) ? new PlanResponseDto(plan) : null;

        List<PlaceDto> places = (plan != null)
                ? plan.getPlaceEntities().stream().map(PlaceDto::new).toList()
                : null;

        // 인기 플랜
        PlanResponseDto popularPlanDto = (plan != null) ? new PlanResponseDto(plan) : null;

        return new CommunityPostResponseDto(
                post.getPostId(),
                post.getUserEntity().getUserId(),
                post.getTitle(),
                post.getPostContent(),
                post.getIsTravelPlan(),
                (plan != null) ? plan.getPlanId() : null,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                regionThumbnail,
                post.getUserEntity().getNickname(),
                planDto,
                places,
                popularPlanDto
        );
    }
}

