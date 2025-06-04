package com.Travelrithm.planbuilder;

import com.Travelrithm.domain.TransportMode;
import com.Travelrithm.planbuilder.dto.front.DayMap;
import com.Travelrithm.planbuilder.dto.front.EditPlanner;
import com.Travelrithm.planbuilder.dto.front.Location;
import com.Travelrithm.planbuilder.dto.kakao.mobility.DestinationResponseDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.DestinationRequestDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WaypointRequestDto;
import com.Travelrithm.planbuilder.dto.publicdata.CommonResponseDto;
import com.Travelrithm.planbuilder.dto.publicdata.DataRequestDto;
import com.Travelrithm.planbuilder.dto.publicdata.Item;
import com.Travelrithm.planbuilder.dto.publicdata.TotalResponseDto;
import com.Travelrithm.planbuilder.kakaomobility.KakaoMobilityService;

import com.Travelrithm.planbuilder.publicdata.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanGenerator {

    private String avgLat;
    private String avgLon;
    private Double avgRadius = 5000.0;
    private String travelDestination;
    private String preference="자연";
    private TransportMode transportMode;
    private Integer fatigue;
    private Integer weight;
    Map<String, String> CATEGORY_MAP = Map.of(
            "자연", "A0101",
            "역사", "A0201",
            "휴양", "A0202",
            "체험", "A0203",
            "문화", "A0206",
            "액티비티", "A0301",
            "음식점", "A0502",
            "카페", "A05020900"
    );

    private final KakaoMobilityService kakaoMobilityService;
    private final PublicService publicService;

    public TotalResponseDto generatePlan(EditPlanner editPlanner) {
        this.travelDestination = editPlanner.travelDestination();
        this.transportMode = editPlanner.transportMode();
        this.preference = editPlanner.preference();
        this.fatigue=editPlanner.fatigue().equals("관광")?-11:-8;
        List<DayMap> dayMapList = editPlanner.dayMapList();
        List<Location> allLocations = new ArrayList<>();
        for (DayMap dayMap : dayMapList) {
            for (DayMap.Content content : dayMap.content()) {
                allLocations.add(content.locations());
            }
        }
        log.info(String.valueOf(allLocations.size()));
        Map<String, Double> avgResult = calLocation(allLocations);

        avgLat = String.valueOf(avgResult.get("avgLat"));
        avgLon = String.valueOf(avgResult.get("avgLon"));
        avgRadius = avgResult.get("avgRadius");
        if (avgRadius > 200000.0) avgRadius = 200000.0;
        if (avgRadius < 5000.0) avgRadius = 5000.0;

        return greedyAlgorithm(dayMapList, fatigue);
    }


    public Map<String, Double> calLocation(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            System.out.println("위치 정보가 없습니다.");
            return null;
        }

        double sumLat = 0, sumLon = 0;
        for (Location loc : locations) {
            sumLat += loc.y();  // 위도
            sumLon += loc.x();  // 경도
        }
        double avgLat = sumLat / locations.size();
        double avgLon = sumLon / locations.size();

        double totalDistance = 0;
        for (Location loc : locations) {
            totalDistance += plainDistance(avgLat, avgLon, loc.y(), loc.x());
        }

        double avgRadius = totalDistance / locations.size();

        System.out.printf("중심점 위도: %.6f, 경도: %.6f\n", avgLat, avgLon);
        System.out.printf("평균 반지름 (좌표 기준): %.6f\n", avgRadius);
        Map<String, Double> calResult = new HashMap<>();
        calResult.put("avgLat", avgLat);
        calResult.put("avgLon", avgLon);
        calResult.put("avgRadius", avgRadius);

        return calResult;
    }

    public TotalResponseDto greedyAlgorithm(List<DayMap> dayMapList, Integer fati) {
        Map<String, List<DayMap.Content>> result = new HashMap<>();
        Map<String, List<Item>> commonResponseDtos;
        List<WayPointResponseDto> wayPointResponseDtos = new ArrayList<>();
        for (int dayIndex = 0; dayIndex < dayMapList.size(); dayIndex++) {
            List<DayMap.Content> contents = dayMapList.get(dayIndex).content();
            int size = contents.size();
            fatigue = fati;
            weight = 0;
            double[][] path = new double[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == j) continue;

                    Location origin = contents.get(i).locations();
                    Location destination = contents.get(j).locations();

                    String originStr = origin.x() + "," + origin.y();
                    String destinationStr = destination.x() + "," + destination.y();

                    DestinationResponseDto response = kakaoMobilityService.getPath(
                            new DestinationRequestDto(originStr, destinationStr)
                    );

                    int distance = response.routes().getFirst().summary().distance();
                    path[i][j] = distance;
                }
            }
            Arrays.stream(path)
                    .forEach(arr -> System.out.println(Arrays.toString(arr)));

            int originIdx = 0;
            List<Integer> sortedIndices = IntStream.range(0, size)
                    .boxed()
                    .sorted(Comparator.comparingDouble(i -> path[originIdx][i]))
                    .toList();

            List<DayMap.Content> sortedContent = sortedIndices.stream()
                    .map(contents::get)
                    .toList();

            Location originLocaiton = sortedContent.getFirst().locations();
            List<Location> list = sortedContent.subList(1, sortedContent.size() - 1).stream().map(DayMap.Content::locations).toList();
            Location destinationLocaiton = sortedContent.getLast().locations();

            WaypointRequestDto waypointRequestDto = new WaypointRequestDto(originLocaiton, destinationLocaiton, list);
            WayPointResponseDto paths = kakaoMobilityService.getPaths(waypointRequestDto);
            wayPointResponseDtos.add(paths);
            sortedContent
                    .forEach(this::calFatitgue);

            result.put("day" + dayIndex, sortedContent);

        }
        commonResponseDtos = translatePrefer(preference);
        return new TotalResponseDto(commonResponseDtos, result, wayPointResponseDtos);

    }

    private Map<String, List<Item>> translatePrefer(String preference) {
        Map<String, List<Item>> commonResponseDtos = new HashMap<>();
        log.info("getCategory start");
        if (preference.equals("자연")) {
            if (fatigue <= 3 && fatigue >= -3) {
                commonResponseDtos.put("자연 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("자연"))));
                commonResponseDtos.put("역사 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("역사"))));
                commonResponseDtos.put("휴양 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("휴양"))));
                commonResponseDtos.put("음식점", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("음식점"))));
            }
            if (fatigue > 3) {
                commonResponseDtos.put("휴양 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("휴양"))));
                commonResponseDtos.put("음식점", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("음식점"))));

            }
            if (fatigue < -3) {
                commonResponseDtos.put("자연 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("자연"))));
                commonResponseDtos.put("역사 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("역사"))));
            }
        }
        return commonResponseDtos;
    }

    private void calFatitgue(DayMap.Content content) {
        weight+=1; //하나의 장소를 방문할 때마다 가중치가 1이 올라감, 피로도에 맞춰 장소를 무한으로 늘려도 일정량 이상이면 피로도가 양수값이 될 수밖에 없음
        switch (content.category()) {
            case "CT1", "AT4" -> fatigue += 3+weight;
            case "FD6" -> fatigue += -1+weight;
            case "CE7" -> fatigue += -2+weight;
            default -> {
                fatigue+=1+weight;
            }
        }
    }
    private double plainDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // 지구 반지름 (단위: m)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
