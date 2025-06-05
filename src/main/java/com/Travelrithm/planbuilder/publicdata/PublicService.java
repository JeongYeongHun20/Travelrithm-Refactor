package com.Travelrithm.planbuilder.publicdata;


import com.Travelrithm.planbuilder.dto.publicdata.CommonResponseDto;
import com.Travelrithm.planbuilder.dto.publicdata.DataRequestDto;
import com.Travelrithm.planbuilder.dto.publicdata.DataResponseDto;
import com.Travelrithm.planbuilder.dto.publicdata.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicService {

    @Value("${data.service_key}")
    private String serviceKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String DATA_URL = "https://apis.data.go.kr/B551011/KorService2";

    public List<Item> getCategory(DataRequestDto dataRequestDto) {
        String cat1 = dataRequestDto.category().substring(0, 3);
        String cat2 = dataRequestDto.category();

        log.info(cat1+cat2);
        log.info("getCategory middle");
        URI uri = UriComponentsBuilder.fromHttpUrl(DATA_URL)
                .path("/locationBasedList2")
                .queryParam("MobileOS", "WEB")
                .queryParam("MobileApp", "Travelrithm")
                .queryParam("_type", "json")
                .queryParam("mapX", dataRequestDto.mapX())
                .queryParam("mapY", dataRequestDto.mapY())
                .queryParam("radius", dataRequestDto.radius())
                .queryParam("cat1", cat1)
                .queryParam("cat2", cat2)
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();
        log.info(uri.toString());

        DataResponseDto response = Optional.ofNullable(restTemplate.getForObject(uri, DataResponseDto.class))
                .orElse(null);
        if (response == null) return List.<Item>of((Item) null);

        log.info("getCategory end: "+response.toString());

        return getCategoryCommon(response);
    }

    public List<Item> getCategoryCommon(DataResponseDto dataResponseDto) {
        log.info("getCategoryCommon start");
        List<DataResponseDto.Response.Body.Items.Item> items = dataResponseDto.response().body().items().item();
        List<Item> resultItem = new ArrayList<>();
        log.info("getCategoryCommon middle");

        for(int i=0;i<items.size();i++) {
            log.info(items.get(i).contentid());
            int finalI = i;
            URI uri = UriComponentsBuilder.fromHttpUrl(DATA_URL)
                    .path("/detailCommon2")
                    .queryParam("MobileOS", "WEB")
                    .queryParam("MobileApp", "Travelrithm")
                    .queryParam("_type", "json")
                    .queryParam("contentId", items.get(finalI).contentid())
                    .queryParam("serviceKey", serviceKey)
                    .build(true)
                    .toUri();

            CommonResponseDto response = restTemplate.getForObject(uri, CommonResponseDto.class);
            assert response != null;
            resultItem.add(response.response().body().items().item().getFirst());

        }
        return resultItem;
    }
}