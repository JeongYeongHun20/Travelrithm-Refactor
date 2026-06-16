package com.Travelrithm.planBuilderV2.dto;

import com.Travelrithm.publicdata.dto.RegionLocationCategory;

import java.util.List;

public record GeneratedPlan(
        int day,
        List<SelectedPlace> selectedPlaces,
        List<RegionLocationCategory> categories,
        List<GeneratedRoute> routes
) {
}
