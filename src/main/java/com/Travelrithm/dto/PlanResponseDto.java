package com.Travelrithm.dto;

import com.Travelrithm.domain.Plan;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public record PlanResponseDto(
        Integer planId,
        String sigunguCd,
        String sigunguName,
        LocalDate startDate,
        LocalDate endDate,
        Integer companionCount,
        String companionType,
        String travelTaste,
        String travelPurpose,
        String transportMode,
        List<PlaceDto> places,
        String nickname
) {
    public PlanResponseDto(Plan plan){
        this(
                plan.getPlanId(),
                plan.getRegion().getSigunguCd(),
                plan.getRegion().getSigunguName(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getCompanionCount(),
                plan.getCompanionType().name(),
                plan.getTravelTaste().name(),
                plan.getTravelPurpose().name(),
                plan.getTransportMode().name(),
                plan.getPlaceEntities().stream().map(PlaceDto::new).toList(),
                plan.getMember().getNickname()
        );
    }
    public PlanResponseDto(Plan plan, String postContent) {
        this(
                plan.getPlanId(),
                plan.getRegion().getSigunguCd(),
                plan.getRegion().getSigunguName(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getCompanionCount(),
                plan.getCompanionType().name(),
                plan.getTravelTaste().name(),
                plan.getTravelPurpose().name(),
                plan.getTransportMode().name(),
                plan.getPlaceEntities().stream().map(PlaceDto::new).toList(),
                plan.getMember().getNickname()
        );
    }
    public static PlanResponseDto from(Plan plan){
        return new PlanResponseDto(plan);
    }
}

