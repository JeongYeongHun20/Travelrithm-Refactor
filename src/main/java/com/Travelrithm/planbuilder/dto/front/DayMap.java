package com.Travelrithm.planbuilder.dto.front;


import com.Travelrithm.planbuilder.dto.publicdata.Item;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

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
