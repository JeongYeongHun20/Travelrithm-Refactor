package com.Travelrithm.planbuilder;

import com.Travelrithm.domain.TransportMode;
import com.Travelrithm.planbuilder.dto.front.*;
import com.Travelrithm.planbuilder.dto.kakao.mobility.*;
import com.Travelrithm.planbuilder.dto.publicdata.DataRequestDto;
import com.Travelrithm.planbuilder.dto.publicdata.Item;
import com.Travelrithm.planbuilder.dto.publicdata.TotalResponseDto;
import com.Travelrithm.planbuilder.dto.tmap.TmapPathRequestDto;
import com.Travelrithm.planbuilder.dto.tmap.TmapPathResponseDto;
import com.Travelrithm.planbuilder.kakaomobility.KakaoMobilityService;

import com.Travelrithm.planbuilder.publicdata.PublicService;
import com.Travelrithm.planbuilder.tmap.TmapPathService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanGenerator {

    private final KakaoMobilityService kakaoMobilityService;
    private final PublicService publicService;
    private final TmapPathService tmapPathService;

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

        if(allLocations.size()==1){ //사용자 입력 장소가 하나일때
            avgLat = String.valueOf(allLocations.getFirst().y());
            avgLon = String.valueOf(allLocations.getFirst().x());
        }else{
            Map<String, Double> avgResult = calLocation(allLocations);
            avgLat = String.valueOf(avgResult.get("avgLat"));
            avgLon = String.valueOf(avgResult.get("avgLon"));
            avgRadius = avgResult.get("avgRadius");
        }

        if (avgRadius > 200000.0) avgRadius = 200000.0;
        if (avgRadius < 5000.0) avgRadius = 5000.0;

        return greedyAlgorithm(dayMapList, fatigue, transportMode);
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

    public TotalResponseDto greedyAlgorithm(List<DayMap> dayMapList, Integer fati, TransportMode transportMode) {
        Map<Integer, List<DayMap.Content>> result = new HashMap<>();
        Map<Integer, Map<String, List<Item>>> commonResponseDtos = new HashMap<>();
        Map<Integer, List<TmapPathResponseDto>> busPathResponseDtos = new HashMap<>();
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
            log.info("sortedIndices success");

            List<DayMap.Content> sortedContent = sortedIndices.stream()
                    .map(contents::get)
                    .toList();

            if(size>1){
                if (transportMode == TransportMode.transit) {
                    Location BusoriginLocaiton = sortedContent.getFirst().locations();
                    List<Location> Buslists = sortedContent.subList(1, sortedContent.size()).stream().map(DayMap.Content::locations).toList();
                    List<TmapPathResponseDto> tmapPathResponseDtos = new ArrayList<>();
                    for (Location content : Buslists) {
                        TmapPathResponseDto tmapPathResponseDto = tmapPathService.getPath(new TmapPathRequestDto(
                                String.valueOf(BusoriginLocaiton.x()),
                                String.valueOf(BusoriginLocaiton.y()),
                                String.valueOf(content.x()),
                                String.valueOf(content.y()),
                                "json",
                                1,
                                ""));
                        BusoriginLocaiton = content;
                        tmapPathResponseDtos.add(tmapPathResponseDto);
                    }
                    busPathResponseDtos.put(dayIndex, tmapPathResponseDtos);
                }

                Location originLocaiton = sortedContent.getFirst().locations();
                List<Location> list = sortedContent.subList(1, sortedContent.size() - 1).stream().map(DayMap.Content::locations).toList();
                Location destinationLocaiton = sortedContent.getLast().locations();

                if (transportMode == TransportMode.car) {
                    WaypointRequestDto waypointRequestDto = new WaypointRequestDto(originLocaiton, destinationLocaiton, list, 2, true);
                    WayPointResponseDto paths = kakaoMobilityService.getPaths(waypointRequestDto);
                    wayPointResponseDtos.add(paths);
                }
                sortedContent
                        .forEach(this::calFatitgue);
            }

            result.put(dayIndex, sortedContent);
            commonResponseDtos.put(dayIndex, translatePrefer(preference));
        }
        if(transportMode==TransportMode.car)
            return new TotalResponseDto(commonResponseDtos, result, wayPointResponseDtos, null);
        else
            return new TotalResponseDto(commonResponseDtos, result, null, busPathResponseDtos);
    }

    private Map<String, List<Item>> translatePrefer(String preference) {
        Map<String, List<Item>> commonResponseDtos = new HashMap<>();
        log.info("getCategory start");
        if (preference.equals("nature")) {
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
        if (preference.equals("culture")) {
            if (fatigue <= 3 && fatigue >= -3) {
                commonResponseDtos.put("문화 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("문화"))));
                commonResponseDtos.put("체험 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("체험"))));
                commonResponseDtos.put("휴양 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("휴양"))));
                commonResponseDtos.put("음식점", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("음식점"))));
            }
            if (fatigue > 3) {
                commonResponseDtos.put("휴양 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("휴양"))));
                commonResponseDtos.put("음식점", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("음식점"))));

            }
            if (fatigue < -3) {
                commonResponseDtos.put("문화 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("문화"))));
                commonResponseDtos.put("체험 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("체험"))));
            }
        }
        if (preference.equals("activity")) {
            if (fatigue <= 3 && fatigue >= -3) {
                commonResponseDtos.put("액티비티 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("액티비티"))));
                commonResponseDtos.put("휴양 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("휴양"))));
                commonResponseDtos.put("음식점", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("음식점"))));
            }
            if (fatigue > 3) {
                commonResponseDtos.put("휴양 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("휴양"))));
                commonResponseDtos.put("음식점", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("음식점"))));

            }
            if (fatigue < -3) {
                commonResponseDtos.put("액티비티 관광지", publicService.getCategory(new DataRequestDto(avgLon, avgLat, String.valueOf(avgRadius), CATEGORY_MAP.get("액티비티"))));
            }
        }
        return commonResponseDtos;
    }

    public CompleteResponseDto completePlan(CompletePlanner completePlanner) {
        List<PlaceInfo> placeInfo = new ArrayList<>();
        List<DayMap> stringListMap = completePlanner.dayMapList();
        Map<Integer, List<Item>> stringListMap1 = completePlanner.itemList();
        TransportMode transportMode = completePlanner.transportMode();

        for (int dayIndex = 0; dayIndex < stringListMap.size(); dayIndex++) {
            List<DayMap.Content> contents = stringListMap.get(dayIndex).content();
            List<Item> items = stringListMap1.get(dayIndex);
            List<PlaceInfo.Place> places = new ArrayList<>();

            for (DayMap.Content content : contents) {
                places.add(new PlaceInfo.Place(content.keyword(),content.img(), content.locations().x(), content.locations().y()));
            }

            if (items != null) {
                for (Item item : items) {
                    places.add(new PlaceInfo.Place(item.title(), !item.firstimage().isEmpty() ? item.firstimage() : item.firstimage2(), safeParse(item.mapx()), safeParse(item.mapy())));
                }
            };



            placeInfo.add(new PlaceInfo(dayIndex, places));
        }
        for (PlaceInfo p : placeInfo) {
            for(int i=0;i<p.getPlaces().size();i++){
                log.info("{}-{}: {}|{}",p.getDay(), p.getPlaces().get(i).getPlaceName(), p.getPlaces().get(i).getX(), p.getPlaces().get(i).getY());
            }

        }
        return calculateOptimizedPaths(placeInfo,transportMode);
    }
    public CompleteResponseDto calculateOptimizedPaths(List<PlaceInfo> placeInfoList,TransportMode transportMode) {
        List<WayPointResponseDto> wayPointResponseDtos = new ArrayList<>();
        List<List<PlaceInfo.Place>> completePlaces = new ArrayList<>();
        List<List<TmapPathResponseDto>> busPointResponsDtos = new ArrayList<>();
        log.info("calculateOptimizedPaths: start");
        for (PlaceInfo placeInfo : placeInfoList) {
            List<PlaceInfo.Place> places = placeInfo.getPlaces();
            int size = places.size();
            if (size < 2) continue;
            log.info(String.valueOf(size));
            // 거리 행렬 생성
            double[][] path = new double[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == j) continue;

                    String origin = places.get(i).getX() + "," + places.get(i).getY();
                    String destination = places.get(j).getX() + "," + places.get(j).getY();
                    DestinationResponseDto response = kakaoMobilityService.getPath(
                            new DestinationRequestDto(origin, destination)
                    );
                    log.info(response.toString());
                    if(response.routes().getFirst().result_code()!=0)
                        break;
                    int distance = response.routes().getFirst().summary().distance();
                    path[i][j] = distance;
                }
            }

            // 출발지: 첫 번째 장소, 나머지 중 가까운 순으로 정렬
            int originIdx = 0;
            List<Integer> sortedIndices = IntStream.range(0, size)
                    .boxed()
                    .sorted(Comparator.comparingDouble(i -> path[originIdx][i]))
                    .toList();

            List<PlaceInfo.Place> sortedPlaces = sortedIndices.stream()
                    .map(places::get)
                    .toList();

            if(transportMode==TransportMode.transit){
                CompleteLocation busOrigin = new CompleteLocation(sortedPlaces.getFirst().getX(), sortedPlaces.getFirst().getY(),sortedPlaces.getFirst().getPlaceName());
                List<CompleteLocation> busPoints = sortedPlaces.subList(1, sortedPlaces.size()).stream()
                        .map(p -> new CompleteLocation(p.getX(), p.getY(), p.getPlaceName()))
                        .toList();
                List<TmapPathResponseDto> tmapPathResponseDtos = new ArrayList<>();
                for (CompleteLocation content : busPoints) {
                    TmapPathResponseDto tmapPathResponseDto = tmapPathService.getPath(new TmapPathRequestDto(
                            String.valueOf(busOrigin.x()),
                            String.valueOf(busOrigin.y()),
                            String.valueOf(content.x()),
                            String.valueOf(content.y()),
                            "json",
                            1,
                            ""));
                    busOrigin = content;
                    tmapPathResponseDtos.add(tmapPathResponseDto);
                }
                busPointResponsDtos.add(tmapPathResponseDtos);
            }
            if(transportMode==TransportMode.car){// 출발, 경유지, 도착으로 나눔
                CompleteLocation origin = new CompleteLocation(sortedPlaces.getFirst().getX(), sortedPlaces.getFirst().getY(), sortedPlaces.getFirst().getPlaceName());
                List<CompleteLocation> waypoints = sortedPlaces.subList(1, sortedPlaces.size() - 1).stream()
                        .map(p -> new CompleteLocation(p.getX(), p.getY(), p.getPlaceName()))
                        .toList();
                CompleteLocation destination = new CompleteLocation(sortedPlaces.getLast().getX(), sortedPlaces.getLast().getY(), sortedPlaces.getFirst().getPlaceName());

                // API 요청
                CompleteWaypointRequestDto request = new CompleteWaypointRequestDto(origin, destination, waypoints,2, true);
                WayPointResponseDto responseDto = kakaoMobilityService.getPaths(request);

                wayPointResponseDtos.add(responseDto);
            }
            completePlaces.add(sortedPlaces);
        }
        CompleteResponseDto completeResponseDto;
        if(transportMode==TransportMode.car)
            completeResponseDto = new CompleteResponseDto(wayPointResponseDtos,null , completePlaces);
        else
            completeResponseDto = new CompleteResponseDto(null, busPointResponsDtos , completePlaces);

        return completeResponseDto;
    }

    private double safeParse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
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
