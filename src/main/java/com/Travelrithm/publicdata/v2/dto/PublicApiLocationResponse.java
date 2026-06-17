package com.Travelrithm.publicdata.v2.dto;

import com.Travelrithm.planbuilder.dto.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicApiLocationResponse(
        String contentid,
        String title,
        String firstimage,
        String firstimage2,
        String mapx,
        String mapy,
        String overview
){
    public Location toLocation(){
        return new Location(
                Double.parseDouble(this.mapx),
                Double.parseDouble(this.mapy)
        );
    }
}