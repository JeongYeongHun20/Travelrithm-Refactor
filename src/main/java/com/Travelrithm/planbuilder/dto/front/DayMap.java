package com.Travelrithm.planbuilder.dto.front;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DayMap(
    List<Content> content,
    int day

) {
    public record Content(
            String keyword,
            Location locations,
            String category,
            String description,
            Img img
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
