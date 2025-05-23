package com.Travelrithm.planbuilder.dto.kakao.place;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record KakaoPlaceRequestDto(
        String placeName,
        double latitude,
        double longitude
) {}