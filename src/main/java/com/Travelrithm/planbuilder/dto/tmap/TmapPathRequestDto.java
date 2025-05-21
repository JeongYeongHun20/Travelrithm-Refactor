package com.Travelrithm.planbuilder.dto.tmap;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmapPathRequestDto(
        String startX,       // 출발지 경도 (WGS84)
        String startY,       // 출발지 위도 (WGS84)
        String endX,         // 도착지 경도 (WGS84)
        String endY,         // 도착지 위도 (WGS84)
        String format,       // 응답 포맷 (json 또는 xml), 기본값은 json
        Integer count,       // 최대 응답 결과 개수 (1~10), 기본값은 10
        String lang    // 검색 날짜 및 시간 (형식: yyyymmddhhmm)
) {}
