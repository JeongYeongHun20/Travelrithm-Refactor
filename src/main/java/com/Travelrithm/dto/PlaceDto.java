package com.Travelrithm.dto;



import com.Travelrithm.domain.Place;

import java.math.BigDecimal;


public record PlaceDto(
        Integer placeId,// 기존 장소라면 ID 포함, 새로 추가되면 null
        String placeName,
        String placeAddress,
        BigDecimal lat,
        BigDecimal lng,
        String memo,
        Integer day,
        Integer sequence,
        String category
) {
    public PlaceDto(Place entity) {
        this(
                entity.getId(),
                entity.getPlaceName(),
                entity.getPlaceAddress(),
                entity.getLat(),
                entity.getLng(),
                entity.getMemo(),
                entity.getDay(),
                entity.getSequence(),
                entity.getCategory()
        );
    }
}
