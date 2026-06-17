package com.Travelrithm.publicdata.v2.dto;

import com.Travelrithm.planbuilder.dto.Location;

public record RegionLocation(
        String contentid,
        String title,
        String firstimage,
        String firstimage2,
        String overview,
        Location location
) {
    public static RegionLocation from(PublicApiLocationResponse publicApiLocationResponse) {
        return new RegionLocation(
                publicApiLocationResponse.contentid(),
                publicApiLocationResponse.title(),
                publicApiLocationResponse.firstimage(),
                publicApiLocationResponse.firstimage2(),
                publicApiLocationResponse.overview(),
                publicApiLocationResponse.toLocation()
        );
    }
}
