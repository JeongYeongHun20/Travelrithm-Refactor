package com.Travelrithm.dto;

import com.Travelrithm.domain.Region;

public record RegionDto(
    String sigunguCd,
    String sigungu_name,
    String areaCd,
    String areaName
) {
    public RegionDto(Region region) {
        this(
                region.getSigunguCd(),
                region.getSigunguName(),
                region.getAreaCd(),
                region.getAreaName()
        );
    }

}