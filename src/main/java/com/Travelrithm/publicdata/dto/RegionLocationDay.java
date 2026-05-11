package com.Travelrithm.publicdata.dto;

import java.util.List;

public record RegionLocationDay(
        int day,
        List<RegionLocation> locations
) {
}
