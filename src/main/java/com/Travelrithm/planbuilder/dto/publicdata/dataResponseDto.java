package com.Travelrithm.planbuilder.dto.publicdata;


import java.util.List;

public record dataResponseDto(
        Items items,
        int totalCount
) {
    public record Items(
            List<Item> item
    ) {
        public record Item(
                String contentid, //place id 혹시몰라 넣어놨는데 쓸필요 없을듯
                String title,   //장소 이름
                String addr1,  //장소 위치 1
                String addr2,   //장소 위치 2, addr1+addr2 더해서 보여주면 될 듯
                String mapx,    //경도
                String mapy,    //위도
                String overview  //장소 요약
        ) {}
    }
}