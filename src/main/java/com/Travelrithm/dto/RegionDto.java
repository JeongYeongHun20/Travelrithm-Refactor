package com.Travelrithm.dto;

import com.Travelrithm.domain.RegionEntity;

public record RegionDto(
    String sigunguCd,
    String sigungu_name,
    String areaCd,
    String areaName
) {
    public RegionDto(RegionEntity regionEntity) {
        this(
                regionEntity.getSigunguCd(),
                regionEntity.getSigunguName(),
                regionEntity.getAreaCd(),
                regionEntity.getAreaName()
        );
    }

}