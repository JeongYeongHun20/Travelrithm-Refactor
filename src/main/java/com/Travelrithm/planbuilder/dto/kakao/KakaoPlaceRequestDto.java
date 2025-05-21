package com.Travelrithm.planbuilder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoPlaceRequestDto(
        String placeName,
        double latitude,
        double longitude
) {}