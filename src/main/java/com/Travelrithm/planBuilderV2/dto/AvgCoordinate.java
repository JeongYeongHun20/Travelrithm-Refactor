package com.Travelrithm.planBuilderV2.dto;

import com.Travelrithm.planbuilder.dto.Location;

public record AvgCoordinate (
        int day,
        Location location,
        double radius
){
}
