package com.Travelrithm.publicdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommonResponseDto(
       Response response
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            Body body
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Body(
                Items items
        ){
            @JsonIgnoreProperties(ignoreUnknown = true)
            public record Items(
                    List<Item> item

            ){
            }

        }
    }


}
