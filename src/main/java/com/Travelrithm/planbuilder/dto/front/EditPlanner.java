package com.Travelrithm.planbuilder.dto.front;

import com.Travelrithm.domain.TransportMode;

import java.util.List;

public record EditPlanner(
        List<DayMap> dayMapList,
        String travelDestination, //여행갈 지역 ex(서울, 광주 등)
        String preference, //관광 쥐향(자연, 문화, 액티비티)
        String fatigue,// 많이 돌아다니는가, 적게 돌아다니는가(관광형, 휴양형)
        TransportMode transportMode //대중고툥 or bus
) {
}
