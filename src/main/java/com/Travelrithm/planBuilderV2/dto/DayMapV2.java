package com.Travelrithm.planBuilderV2.dto;


import java.util.List;


public record DayMapV2(
        List<Content> content,
        int day

) {
    public record Content(
            String keyword,
            LocationV2 locations,
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
