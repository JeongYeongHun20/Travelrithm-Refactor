package com.Travelrithm.kakaomobility.dto;

import com.Travelrithm.planbuilder.dto.CompleteLocation;

import java.util.List;

public record CompleteWaypointRequestDto(
        CompleteLocation origin,
        CompleteLocation destination,
        List<CompleteLocation> waypoints,
        int roadevent,
        boolean alternatives
) {
}