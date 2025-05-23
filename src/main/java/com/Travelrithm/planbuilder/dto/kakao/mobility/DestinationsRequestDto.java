package com.Travelrithm.planbuilder.dto.kakao.mobility;




import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DestinationsRequestDto(
        Origin origin,
        List<Destination> destinations,
        int radius,
        String priority
) {
    public record Origin(
            String name,
            double x,
            double y
    ) {}

    public record Destination(
            String key,
            double x,
            double y
    ) {}
}

