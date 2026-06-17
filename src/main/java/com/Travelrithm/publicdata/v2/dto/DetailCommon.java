package com.Travelrithm.publicdata.v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DetailCommon(
        Response response
)

{
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Response(
                Body body
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            public record Body(
                    Items items
            ) {
                @JsonIgnoreProperties(ignoreUnknown = true)
                public record Items(
                        Item item
                ) {
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public record Item(
                            String overview
                    ) {
                    }
                }
            }
        }
}
