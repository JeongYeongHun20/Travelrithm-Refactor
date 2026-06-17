package com.Travelrithm.publicdata.v2;


import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.publicdata.PublicDataServiceProvider;
import com.Travelrithm.publicdata.v2.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataServiceV2 implements PublicDataServiceProvider {

    @Value("${data.service_keyV2}")
    private String serviceKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String DATA_URL = "https://apis.data.go.kr/B551011/KorService2";
    private final String LOCATION_PATH="/locationBasedList2";
    private final String DETAIL_PATH="/detailCommon2";
    private final Map<String, Map<String, String>> CATEGORY_MAP = Map.of(
            "nature", Map.of(
                    "자연 관광지", "A0101",
                    "역사 관광지", "A0201",
                    "휴양 관광지", "A0202",
                    "음식점", "A0502"
            ),
            "culture", Map.of(
                    "문화 관광지", "A0206",
                    "체험 관광지", "A0203",
                    "휴양 관광지", "A0202",
                    "음식점", "A0502"
            ),
            "activity", Map.of(
                    "액티비티 관광지", "A0301",
                    "휴양 관광지", "A0202",
                    "음식점", "A0502"
            )
    );

    public RegionLocationResponse getCategory(List<AvgCoordinate> avgCoordinates, String preference) {//일자별 기본 정보 요청
        log.info("Enter: getCategory");
        List<RegionLocationDay> result = new ArrayList<>();
        Map<String, String> categories = CATEGORY_MAP.getOrDefault(preference, CATEGORY_MAP.get("nature"));

        for (AvgCoordinate avgCoordinate : avgCoordinates) {
            List<RegionLocationCategory> regionLocationCategories = categories.entrySet().stream()
                    .map(category -> new RegionLocationCategory(
                            category.getKey(),
                            requestCategory(avgCoordinate, category.getValue())
                    ))
                    .toList();

            result.add(new RegionLocationDay(
                    avgCoordinate.day(),
                    regionLocationCategories
            ));
        }

        return new RegionLocationResponse(result);
    }
    public String getOverView(String contentId){
        URI uri=UriComponentsBuilder.fromHttpUrl(DATA_URL)
                .path(DETAIL_PATH)
                .queryParam("MobileOS", "WEB")
                .queryParam("MobileApp", "Travelrithm")
                .queryParam("_type", "json")
                .queryParam("contentId", contentId)
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();

        DetailCommon detailCommon = restTemplate.getForObject(uri, DetailCommon.class);
        if (detailCommon == null ||
                detailCommon.response() == null ||
                detailCommon.response().body() == null ||
                detailCommon.response().body().items() == null ||
                detailCommon.response().body().items().item() == null) {
            return "상세정보 없음";
        }
        return detailCommon.response().body().items().item().overview();
    }

    private List<RegionLocation> requestCategory(AvgCoordinate avgCoordinate, String category) {
        String cat1 = category.substring(0, 3);
        String cat2 = category;
        log.info("location x,y: {} {}", avgCoordinate.location().x(), avgCoordinate.location().y());
        URI uri = UriComponentsBuilder.fromHttpUrl(DATA_URL)
                .path(LOCATION_PATH)
                .queryParam("MobileOS", "WEB")
                .queryParam("MobileApp", "Travelrithm")
                .queryParam("_type", "json")
                .queryParam("mapX", avgCoordinate.location().x())
                .queryParam("mapY", avgCoordinate.location().y())
                .queryParam("radius", String.valueOf((int) avgCoordinate.radius()))
                .queryParam("cat1", cat1)
                .queryParam("cat2", cat2)
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();

        log.info("Request category URL: {}", uri);
        LocationBasedListResponseDto responseBody = restTemplate.getForObject(uri, LocationBasedListResponseDto.class);
        assert responseBody != null : "응답 객체가 없습니다";
        return responseBody.response()
                .body()
                .items()
                .item()
                .stream()
                .map(RegionLocation::from)
                .toList();

    }
}
