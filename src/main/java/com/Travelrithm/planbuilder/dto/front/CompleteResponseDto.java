package com.Travelrithm.planbuilder.dto.front;

import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;
import com.Travelrithm.planbuilder.dto.tmap.TmapPathResponseDto;

import java.util.List;

public record CompleteResponseDto(
        List<WayPointResponseDto> wayPoints, //위 장소들 순서대로 경로값 설정
        List<List<TmapPathResponseDto>> busPoints,
        List<List<PlaceInfo.Place>> completePlaces
) {
}
