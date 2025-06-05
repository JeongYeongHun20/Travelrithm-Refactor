package com.Travelrithm.planbuilder.dto.kakao.mobility;

import com.Travelrithm.planbuilder.dto.front.CompleteLocation;
import com.Travelrithm.planbuilder.dto.front.Location;

import java.util.List;

public record CompleteWaypointRequestDto(
        CompleteLocation origin,
        CompleteLocation destination,
        List<CompleteLocation> waypoints
) {
}