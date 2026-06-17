package com.Travelrithm.publicdata.v2.dto;

import java.util.List;

public record RegionLocationDay(
        int day,
        List<RegionLocationCategory> categories
) {
}
