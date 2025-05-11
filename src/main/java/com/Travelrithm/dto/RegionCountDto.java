package com.Travelrithm.dto;

import com.Travelrithm.domain.RegionEntity;

public record RegionCountDto(
        Integer regionId,
        String regionName,
        String context,
        Long planCount
) {}