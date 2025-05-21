package com.Travelrithm.dto;

import com.Travelrithm.domain.PlanEntity;

import java.time.LocalDate;
import java.util.List;

public record PlanResponseDto(
        Integer planId,
        Integer regionId,
        String regionThumbnailUrl,
        LocalDate startDate,
        LocalDate endDate,
        Integer companionCount,
        String companionType,
        String travelTaste,
        String travelPurpose,
        String transportMode,
        List<PlaceDto> places
) {
    public PlanResponseDto(PlanEntity planEntity) {
        this(
                planEntity.getPlanId(),
                planEntity.getRegionEntity().getRegionId(),
                planEntity.getRegionEntity().getThumbnailImageUrl(),
                planEntity.getStartDate(),
                planEntity.getEndDate(),
                planEntity.getCompanionCount(),
                planEntity.getCompanionType().name(),
                planEntity.getTravelTaste().name(),
                planEntity.getTravelPurpose().name(),
                planEntity.getTransportMode().name(),
                planEntity.getPlaceEntities().stream().map(PlaceDto::new).toList()
        );
    }
}

