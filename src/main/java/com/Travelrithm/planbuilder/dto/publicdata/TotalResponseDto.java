package com.Travelrithm.planbuilder.dto.publicdata;

import com.Travelrithm.planbuilder.dto.front.DayMap;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;

import java.util.List;
import java.util.Map;

public record TotalResponseDto(
        List<CommonResponseDto> commonResponseDtos,
        Map<String, List<DayMap.Content>> result,
        List<WayPointResponseDto> wayPoints
) {
}
