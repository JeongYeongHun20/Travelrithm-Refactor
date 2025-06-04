package com.Travelrithm.planbuilder.dto.publicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataResponseDto(
        Response response
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            Body body
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Body(
                Items items,
                int totalCount
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            public record Items(
                    List<Item> item
            ) {
                @JsonIgnoreProperties(ignoreUnknown = true)
                public record Item(
                        String contentid,
                        String contenttypeid,
                        String title,
                        String addr1,
                        String addr2,
                        String mapX,
                        String mapY
                ) {}
            }
        }
    }
}