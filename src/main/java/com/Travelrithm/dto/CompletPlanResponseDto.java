package com.Travelrithm.dto;

import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;


import java.util.List;
import java.util.Map;

public record CompletPlanResponseDto(
        List<WayPointResponseDto> wayPointResponseDtos,
        Map<Integer, List<PlaceDto>> placesByDay      //    Integer -> n일차, List<PlaceDto> ->sequence 기준 오름차순으로 정령되어있음
) {
}
