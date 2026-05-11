package com.Travelrithm.planBuilderV2.dto;


import com.Travelrithm.planBuilderV2.dto.LocationV2;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DayMapV2(
        List<Content> content,
        int day

) {
    public record Content(
            String keyword,
            LocationV2 locations,
            String category,
            String description,
            String img
    ){
        public record Img(
                String src,
                int width,
                int height,
                String blurDataURL,
                int blurWidth,
                int blurHeight
        ) {}
    }

}
