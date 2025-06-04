package com.Travelrithm.planbuilder.dto.publicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Item(
        String contentid,
        String title,
        String firstimage,
        String firstimage2,
        String mapx,
        String mapy,
        String overview
){

}