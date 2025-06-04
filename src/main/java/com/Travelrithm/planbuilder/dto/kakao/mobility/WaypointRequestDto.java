package com.Travelrithm.planbuilder.dto.kakao.mobility;

import com.Travelrithm.planbuilder.dto.front.Location;

import java.util.List;

public record WaypointRequestDto(
        Location origin,
        Location destination,
        List<Location> waypoints
) {
}