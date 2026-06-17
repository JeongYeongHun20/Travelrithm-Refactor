package com.Travelrithm.publicdata.v2.dto;

import java.util.List;

public record RegionLocationCategory(
        String categoryName,
        List<RegionLocation> locations
) {
}
