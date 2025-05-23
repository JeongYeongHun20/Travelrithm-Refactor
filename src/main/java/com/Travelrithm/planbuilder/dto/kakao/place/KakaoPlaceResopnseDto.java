package com.Travelrithm.planbuilder.dto.kakao.place;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoPlaceResopnseDto(
        Meta meta,
        List<Document> documents
) {

    public record Meta(
            int total_count,
            int pageable_count,
            boolean is_end
    ) {}

    public record Document(
            String id,
            String place_name,
            String category_group_code,
            String category_group_name,
            String category_name,
            String address_name,
            String road_address_name,
            String x,
            String y
    ) {}
}