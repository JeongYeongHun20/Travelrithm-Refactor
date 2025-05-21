package com.Travelrithm.planbuilder.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoPlaceRequestDto(
        String placeName,
        double latitude,
        double longitude
) {}