package com.Travelrithm.publicdata.v3;


import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.publicdata.PublicDataServiceProvider;
import com.Travelrithm.publicdata.v2.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataServiceV3 implements PublicDataServiceProvider {
    private final PublicDataClient publicDataClient;
    private final static Map<String, Map<String, String>> CATEGORY_MAP = Map.of(
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
            List<RegionLocationCategory> regionLocationCategories = categories
                    .entrySet()
                    .stream()
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
        log.info("Exit: getCategory");
        return new RegionLocationResponse(result);
    }
    public String getOverView(String contentId){
        DetailCommon detailCommon = publicDataClient.fetchDetailCommon(contentId);
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
        log.info("location x,y: {} {}", avgCoordinate.location().x(), avgCoordinate.location().y());
        LocationBasedListResponseDto responseBody = publicDataClient.fetchLocationBasedList(avgCoordinate, cat1, category);

        return responseBody.response()
                .body()
                .items()
                .item()
                .stream()
                .map(RegionLocation::from)
                .toList();

    }
}
