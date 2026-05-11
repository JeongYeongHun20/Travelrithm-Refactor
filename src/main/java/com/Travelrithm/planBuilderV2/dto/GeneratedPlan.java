package com.Travelrithm.planBuilderV2.dto;

import com.Travelrithm.publicdata.dto.RegionLocation;

import java.util.List;

public record GeneratedPlan(
        int day,
        List<DayMapV2.Content> contents,
        List<RegionLocation> categories,
        List<GeneratedRoute> routes
) {
}
