package com.Travelrithm.dto;


import com.Travelrithm.domain.PlanEntity;



import java.time.LocalDate;


public record PlanResponseDto(
        Integer planId,
        Integer regionId,
        LocalDate startDate,
        LocalDate endDate
) {
    public PlanResponseDto(PlanEntity planEntity){
        this(
                planEntity.getPlanId(),
                planEntity.getRegionEntity().getRegionId(),
                planEntity.getStartDate(),
                planEntity.getEndDate()
        );
    }
}
