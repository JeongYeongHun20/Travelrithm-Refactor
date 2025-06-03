package com.Travelrithm.planbuilder.dto.publicdata;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataResponseDto(
        String category,
        Items items,
        int totalCount
) {
    public record Items(
            List<Item> item
    ) {
        public record Item(
                String contentid,
                String contenttypeid,
                String title,   //장소 이름
                String addr1,  //장소 위치 1
                String addr2,   //장소 위치 2, addr1+addr2 더해서 보여주면 될 듯
                String mapX,    //경도
                String mapY    //위도

        ) {}
    }
}