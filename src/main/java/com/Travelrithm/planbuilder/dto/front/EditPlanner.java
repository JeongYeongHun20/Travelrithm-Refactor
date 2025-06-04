package com.Travelrithm.planbuilder.dto.front;

import com.Travelrithm.domain.TransportMode;

import java.util.List;

public record EditPlanner(
        List<DayMap> dayMapList,
        String travelDestination,
        String preference,
        String fatigue,
        TransportMode transportMode
) {
}
