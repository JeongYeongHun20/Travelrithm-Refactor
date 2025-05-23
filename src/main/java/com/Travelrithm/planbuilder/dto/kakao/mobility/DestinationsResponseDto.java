package com.Travelrithm.planbuilder.dto.kakao.mobility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DestinationsResponseDto(
        String trans_id,
        List<Route> routes
) {
    public record Route(
            int result_code,
            String result_msg,
            String key,
            Summary summary
    ) {
        public record Summary(
                int distance,
                int duration
        ) {}
    }
}
