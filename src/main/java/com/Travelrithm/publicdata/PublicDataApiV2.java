package com.Travelrithm.publicdata;


import com.Travelrithm.planBuilderV2.dto.AvgCoordinate;
import com.Travelrithm.publicdata.dto.RegionLocation;
import com.Travelrithm.publicdata.dto.RegionLocationDay;
import com.Travelrithm.publicdata.dto.RegionLocationResponse;
import com.Travelrithm.publicdata.dto.PublicApiLocationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PublicDataApiV2 {

    @Value("${data.service_key}")
    private String serviceKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final String DATA_URL = "https://apis.data.go.kr/B551011/KorService2";
    private final String LOCATION_PATH="/locationBasedList2";
    private final Map<String, String> CATEGORY_MAP = Map.of(
            "nature", "A0101",
            "culture", "A0206",
            "activity", "A0301"
    );

    public RegionLocationResponse getCategory(List<AvgCoordinate> avgCoordinates, String preference) {//일자별 기본 정보 요청
        log.info("Enter: getCategory");
        List<RegionLocationDay> result = new ArrayList<>();
        String category = CATEGORY_MAP.getOrDefault(preference, CATEGORY_MAP.get("nature"));

        for (AvgCoordinate avgCoordinate : avgCoordinates) {
            result.add(new RegionLocationDay(
                    avgCoordinate.day(),
                    requestCategory(avgCoordinate, category)
            ));
        }

        return new RegionLocationResponse(result);
    }

    private List<RegionLocation> requestCategory(AvgCoordinate avgCoordinate, String category) {
        String cat1 = category.substring(0, 3);
        String cat2 = category;
        log.info("location x,y: "+avgCoordinate.location().x()+" "+avgCoordinate.location().y());
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

        String responseBody = restTemplate.getForObject(uri, String.class);
        if (responseBody == null || responseBody.isBlank()) {
            return List.of();
        }

        try {
            return toItems(responseBody);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse location based list response", e);
            return List.of();
        }
    }

    private List<RegionLocation> toItems(String responseBody) throws JsonProcessingException {
        JsonNode itemsNode = objectMapper.readTree(responseBody)
                .path("response")
                .path("body")
                .path("items");
        if (!itemsNode.isObject()) {
            return List.of();
        }

        JsonNode itemNode = itemsNode.path("item");
        if (itemNode.isMissingNode() || itemNode.isNull()) {
            return List.of();
        }

        if (itemNode.isArray()) {
            List<PublicApiLocationResponse> items = objectMapper.convertValue(
                    itemNode,
                    new TypeReference<>() {
                    }
            );
            return items.stream()
                    .map(RegionLocation::from)
                    .toList();
        }

        if (itemNode.isObject()) {
            PublicApiLocationResponse item = objectMapper.convertValue(itemNode, PublicApiLocationResponse.class);
            return List.of(RegionLocation.from(item));
        }

        return List.of();
    }
}
