package com.Travelrithm.planbuilder.dto.tmap;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmapPathResponseDto(
        MetaData metaData // 응답 메타데이터 전체
) {
    public record MetaData(
            RequestParameters requestParameters, // 요청 파라미터
            Plan plan                            // 경로 계획
    ) {}

    public record RequestParameters(
            int busCount,                 // 버스 경로 수
            int expressbusCount,         // 고속버스 경로 수
            int subwayCount,             // 지하철 경로 수
            int airplaneCount,           // 항공 경로 수
            String locale,               // 언어 설정 (예: "ko")
            String endY,                 // 도착지 위도
            String endX,                 // 도착지 경도
            int wideareaRouteCount,      // 광역버스 경로 수
            int subwayBusCount,          // 지하철+버스 혼합 경로 수
            String startY,               // 출발지 위도
            String startX,               // 출발지 경도
            int ferryCount,              // 여객선 경로 수
            int trainCount,              // 기차 경로 수
            String reqDttm               // 요청 시각 (yyyymmddhhMMss)
    ) {}

    public record Plan(
            List<Itinerary> itineraries // 경로 리스트
    ) {}

    public record Itinerary(
            Fare fare,                 // 요금 정보
            int totalTime,            // 총 소요 시간 (초)
            List<Leg> legs,           // 세부 구간 정보
            int totalWalkTime,        // 총 도보 시간 (초)
            int transferCount,        // 환승 횟수
            int totalDistance,        // 총 거리 (m)
            int pathType,             // 경로 유형 (1:지하철, 2:버스 등)
            int totalWalkDistance     // 총 도보 거리 (m)
    ) {}

    public record Fare(
            Regular regular // 일반 요금
    ) {}

    public record Regular(
            int totalFare,     // 총 요금 (원)
            Currency currency  // 통화 정보
    ) {}

    public record Currency(
            String symbol,         // 통화 기호 (₩)
            String currency,       // 통화명 ("원")
            String currencyCode    // 통화 코드 ("KRW")
    ) {}

    public record Leg(
            String mode,           // 이동 수단 (WALK, BUS 등)
            int sectionTime,       // 해당 구간 소요 시간 (초)
            int distance,          // 해당 구간 거리 (m)
            Stop start,            // 구간 시작점
            Stop end,              // 구간 종료점
            List<Step> steps,      // 도보 경로일 경우 이동 경로 정보
            String routeColor,     // 버스 노선 색상 (버스일 경우)
            String route,          // 노선 이름 (버스일 경우)
            int type,              // 노선 유형 (버스일 경우)
            PassStopList passStopList, // 경유 정류장 목록 (버스일 경우)
            PassShape passShape    // 경로 선 정보 (버스일 경우)
    ) {}

    public record Stop(
            String name,   // 장소 이름
            double lon,    // 경도
            double lat     // 위도
    ) {}

    public record Step(
            String streetName,    // 거리 이름
            int distance,         // 거리 길이 (m)
            String description,   // 설명 텍스트
            String linestring     // 경로 선 정보 (좌표들)
    ) {}

    public record PassStopList(
            List<Station> stationList // 정류장 리스트
    ) {}

    public record Station(
            int index,           // 정류장 순번
            String stationName,  // 정류장 이름
            String lon,          // 경도 (문자열로 옴)
            String lat,          // 위도 (문자열로 옴)
            String stationID     // 정류장 ID
    ) {}

    public record PassShape(
            String linestring // 경로 선 정보 (좌표 묶음)
    ) {}
}
