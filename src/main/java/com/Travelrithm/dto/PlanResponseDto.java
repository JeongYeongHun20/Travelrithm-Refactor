package com.Travelrithm.dto;


import com.Travelrithm.domain.PlanEntity;



import java.time.LocalDate;


public record PlanResponseDto(
        Integer planId,
        Integer regionId,
        LocalDate startDate,
        LocalDate endDate,
        Integer companionCount,
        String companionType,
        String travelTaste,
        String travelPurpose,
        String transportMode
) {
    public PlanResponseDto(PlanEntity planEntity){
        this(
                planEntity.getPlanId(),
                planEntity.getRegionEntity().getRegionId(),
                planEntity.getStartDate(),
                planEntity.getEndDate(),
                planEntity.getCompanionCount(),
                planEntity.getCompanionType().name(),
                planEntity.getTravelTaste().name(),
                planEntity.getTravelPurpose().name(),
                planEntity.getTransportMode().name()
        );
    }
}
