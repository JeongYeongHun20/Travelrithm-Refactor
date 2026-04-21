package com.Travelrithm.dto;

import com.Travelrithm.domain.RegionEntity;

public record RegionResponseDto(
    String sigunguCd,
    String sigunguName,
    String areaCd,
    String areaName
) {
    public RegionResponseDto(RegionEntity regionEntity) {
        this(
                regionEntity.getSigunguCd(),
                regionEntity.getSigunguName(),
                regionEntity.getAreaCd(),
                regionEntity.getAreaName()
        );
    }
}
