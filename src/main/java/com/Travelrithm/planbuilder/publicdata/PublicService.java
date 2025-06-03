package com.Travelrithm.planbuilder.publicdata;


import com.Travelrithm.planbuilder.dto.publicdata.DataRequestDto;
import com.Travelrithm.planbuilder.dto.publicdata.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PublicService {

    @Value("${key.service_key}")
    private String serviceKey;
    private final WebClient.Builder webClientBuilder;
    private final String DATA_URL = "https://apis.data.go.kr/B551011/KorService2";

    public void getCategory(DataRequestDto dataRequestDto) {
        String lclsSystm1 = dataRequestDto.category().substring(0, 3);
        String lclsSystm2 = dataRequestDto.category().substring(3);
        WebClient webClient = webClientBuilder
                .baseUrl(DATA_URL)
                .build();
        DataResponseDto dataResponseDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/locationBasedList2")
                        .queryParam("MobileOs", "WEB")
                        .queryParam("MobileApp", "Travelrithm")
                        .queryParam("mapX", dataRequestDto.mapX())
                        .queryParam("mapY", dataRequestDto.mapY())
                        .queryParam("radius", dataRequestDto.radius())
                        .queryParam("lclsSystm1", lclsSystm1)
                        .queryParam("lclsSystm2", lclsSystm2)
                        .queryParam("serviceKey", serviceKey)
                        .build())
                .retrieve()
                .bodyToMono(DataResponseDto.class)
                .block();
        getCategoryComent(dataResponseDto);
        getCategoryImage(dataResponseDto);
    }

    public void getCategoryComent(DataResponseDto dataResponseDto) {

    }
    public void getCategoryImage(DataResponseDto dataResponseDto) {

    }

}