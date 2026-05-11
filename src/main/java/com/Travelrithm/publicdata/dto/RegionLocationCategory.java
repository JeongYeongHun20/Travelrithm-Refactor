package com.Travelrithm.publicdata.dto;

import java.util.List;

public record RegionLocationCategory(
        String categoryName,
        List<RegionLocation> locations
) {
}
