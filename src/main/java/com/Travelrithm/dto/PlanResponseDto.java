package com.Travelrithm.dto;

import com.Travelrithm.domain.PlanEntity;

import java.time.LocalDate;
import java.util.List;

public record PlanResponseDto(
        Integer planId,
        Integer regionId,
        String RegionName,
        String regionThumbnailUrl,
        LocalDate startDate,
        LocalDate endDate,
        Integer companionCount,
        String companionType,
        String travelTaste,
        String travelPurpose,
        String transportMode,
        List<PlaceDto> places,
        List<String> placeNames,
        String postContent
) {
    public PlanResponseDto(PlanEntity planEntity, String postContent) {
        this(
                planEntity.getPlanId(),
                planEntity.getRegionEntity().getRegionId(),
                planEntity.getRegionEntity().getName(),
                planEntity.getRegionEntity().getThumbnailImageUrl(),
                planEntity.getStartDate(),
                planEntity.getEndDate(),
                planEntity.getCompanionCount(),
                planEntity.getCompanionType().name(),
                planEntity.getTravelTaste().name(),
                planEntity.getTravelPurpose().name(),
                planEntity.getTransportMode().name(),
                planEntity.getPlaceEntities().stream().map(PlaceDto::new).toList(),
                planEntity.getPlaceEntities().stream()
                        .map(place -> place.getPlaceName())
                        .toList(),
                postContent
        );
    }
}

