package com.Travelrithm.kakaomap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record KakaoPlaceRequestDto(
        String placeName,
        double latitude,
        double longitude
) {}