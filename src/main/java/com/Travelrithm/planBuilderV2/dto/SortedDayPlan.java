package com.Travelrithm.planBuilderV2.dto;

import java.util.List;

public record SortedDayPlan(
        int day,
        List<SelectedPlace> selectedPlaces
) {
}
