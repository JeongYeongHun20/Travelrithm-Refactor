package com.Travelrithm.dto;

import com.Travelrithm.domain.RegionEntity;

public record RegionResponseDto(
        String regionName,
        String context,
        String thumbnailImageUrl
) {
    public RegionResponseDto(RegionEntity regionEntity) {
        this(
                regionEntity.getName(),
                regionEntity.getContext(),
                regionEntity.getThumbnailImageUrl()
        );
    }
}
