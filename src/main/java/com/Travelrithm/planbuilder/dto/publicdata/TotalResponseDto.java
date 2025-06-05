package com.Travelrithm.planbuilder.dto.publicdata;

import com.Travelrithm.planbuilder.dto.front.DayMap;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;

import java.util.List;
import java.util.Map;

public record TotalResponseDto(
        Map<Integer, Map<String, List<Item>>> commonResponseDtos, //카테고리별 장소들 설명, 및 이미지 파일 차후에 map으로 바꿔서 key값으로 찾게하려고
        Map<Integer, List<DayMap.Content>> result, //front에서 받은 장소들 greedy알고리즘 사용해서 나온 순서대로 리턴
        List<WayPointResponseDto> wayPoints //위 장소들 순서대로 경로값 설정
) {
}
