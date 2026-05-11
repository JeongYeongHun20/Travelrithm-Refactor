package com.Travelrithm.planBuilderV2.dto;

import java.util.List;

public record CalculateRequestDto(
        int day,
        List<LocationV2> locations
){
}
