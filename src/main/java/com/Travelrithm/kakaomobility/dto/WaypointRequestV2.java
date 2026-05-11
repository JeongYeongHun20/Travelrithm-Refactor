package com.Travelrithm.kakaomobility.dto;

import com.Travelrithm.planBuilderV2.dto.LocationV2;

import java.util.List;

public record WaypointRequestV2(
        LocationV2 origin,
        LocationV2 destination,
        List<LocationV2> waypoints,
        int roadevent,
        boolean alternatives
) {
}
