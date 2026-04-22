package com.Travelrithm.kakaomobility.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DestinationRequestDto(
        String origin,
        String destination

) {
}
