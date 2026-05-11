package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityPost;
import com.Travelrithm.domain.Plan;

import java.time.LocalDateTime;
import java.util.List;

public record CommunityPostResponseDto(
        Integer postId,
        Long userId,
        String title,
        String postContent,
        Boolean isTravelPlan,
        Integer planId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String nickname,
        PlanResponseDto plan,
        List<PlaceDto> places,
        PlanResponseDto popularPlan,
        Integer viewCount,
        Integer scrapCount,
        Integer commentCount,
        String sigunguName
) {
    /**
     * 정적 팩토리 메서드: CommunityPostEntity → CommunityPostResponseDto 변환
     */
    public static CommunityPostResponseDto fromEntity(CommunityPost post) {
        Plan plan = post.getPlan();

        PlanResponseDto planDto = (plan != null) ? new PlanResponseDto(plan, null) : null;

        List<PlaceDto> places = (plan != null)
                ? plan.getPlaceEntities().stream().map(PlaceDto::new).toList()
                : null;

        // 인기 플랜
        PlanResponseDto popularPlanDto = (plan != null) ? new PlanResponseDto(plan, null) : null;

        return new CommunityPostResponseDto(
                post.getCommunityPostId(),
                post.getMember().getMemberId(),
                post.getTitle(),
                post.getPostContent(),
                post.getIsTravelPlan(),
                (plan != null) ? plan.getPlanId() : null,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getMember().getNickname(),
                planDto,
                places,
                popularPlanDto,
                post.getViewCount(),
                post.getScrapEntities().size(),
                post.getCommentEntities().size(),
                plan.getRegion().getSigunguName()

        );
    }
}

