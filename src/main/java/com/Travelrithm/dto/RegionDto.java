package com.Travelrithm.dto;

import com.Travelrithm.domain.RegionEntity;

public record RegionDto(
        Integer regionId,
        String name,
        String context,
        Long planCount,
        String thumbnailImageUrl,
        String code
) {
    public RegionDto(RegionEntity regionEntity) {
        this(
                regionEntity.getRegionId(),
                regionEntity.getName(),
                regionEntity.getContext(),
                0L,
                regionEntity.getThumbnailImageUrl(),
                regionEntity.getCode()
        );
    }

}