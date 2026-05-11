package com.Travelrithm.publicdata.dto;

import java.util.List;

public record RegionLocationResponse(
        List<RegionLocationDay> days
) {
}
