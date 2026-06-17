package com.Travelrithm.publicdata.v2.dto;

import java.util.List;

public record RegionLocationResponse(
        List<RegionLocationDay> days
) {
}
