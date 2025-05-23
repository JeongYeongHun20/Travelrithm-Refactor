package com.Travelrithm.planbuilder.dto.kakao.mobility;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DestinationRequestDto(
        String origin,
        String destination,
        String priority

) {
}
