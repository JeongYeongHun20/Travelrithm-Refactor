package com.Travelrithm.kakaomobility.dto;

import com.Travelrithm.planbuilder.dto.Location;

import java.util.List;

public record WaypointRequestDto(
        Location origin,
        Location destination,
        List<Location> waypoints,
        int roadevent,
        boolean alternatives
) {
}