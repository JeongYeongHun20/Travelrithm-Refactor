package com.Travelrithm.dto;

import com.Travelrithm.domain.Region;

public record RegionResponseDto(
    String sigunguCd,
    String sigunguName,
    String areaCd,
    String areaName
) {
    public RegionResponseDto(Region region) {
        this(
                region.getSigunguCd(),
                region.getSigunguName(),
                region.getAreaCd(),
                region.getAreaName()
        );
    }
}
