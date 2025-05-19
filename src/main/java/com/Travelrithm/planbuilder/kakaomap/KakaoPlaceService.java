package com.Travelrithm.planbuilder.kakaomap;


import com.Travelrithm.dto.KakaoUserResponseDto;
import com.Travelrithm.planbuilder.dto.KakaoPlaceRequestDto;
import com.Travelrithm.planbuilder.dto.KakaoPlaceResopnseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class KakaoPlaceService {

    @Value("${kakao.client_id}")
    private String client_id;
    private final WebClient.Builder webClientBuilder;


    private final String KAKAO_SEARCH_URL ="https://dapi.kakao.com/v2/local/search/keyword.JSON";

    public KakaoPlaceResopnseDto getPlaceInfo(KakaoPlaceRequestDto kakaoPlaceRequestDto) {
        WebClient webClient = webClientBuilder
                .baseUrl(KAKAO_SEARCH_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK "+client_id)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", kakaoPlaceRequestDto.placeName())
                        .queryParam("x", kakaoPlaceRequestDto.latitude())
                        .queryParam("y", kakaoPlaceRequestDto.longitude())
                        .build()
                )
                .retrieve()
                .bodyToMono(KakaoPlaceResopnseDto.class)
                .block();
    }

}
