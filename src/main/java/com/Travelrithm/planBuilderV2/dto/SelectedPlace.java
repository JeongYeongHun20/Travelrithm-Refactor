package com.Travelrithm.planBuilderV2.dto;

public record SelectedPlace(
        String keyword,
        LocationV2 locations,
        String category,
        String description,
        Img img
) {
    public record Img(
            String src,
            int width,
            int height,
            String blurDataURL,
            int blurWidth,
            int blurHeight
    ) {}
}
