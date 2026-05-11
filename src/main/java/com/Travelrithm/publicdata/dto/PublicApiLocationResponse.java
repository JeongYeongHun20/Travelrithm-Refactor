package com.Travelrithm.publicdata.dto;

import com.Travelrithm.planbuilder.dto.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicApiLocationResponse(
        String contentid,
        String title,
        String firstimage,
        String firstimage2,
        String mapX,
        String mapY,
        String overview
){
    public Location toLocation(){
        return new Location(
                Double.parseDouble(this.mapX),
                Double.parseDouble(this.mapY)
        );
    }
}