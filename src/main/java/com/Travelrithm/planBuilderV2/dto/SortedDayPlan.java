package com.Travelrithm.planBuilderV2.dto;

import java.util.List;

public record SortedDayPlan(
        int day,
        List<DayMapV2.Content> contents
) {
}
