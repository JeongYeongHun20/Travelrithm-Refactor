package com.Travelrithm.planbuilder.kakaomap;


import com.Travelrithm.planbuilder.dto.kakao.KakaoPlaceRequestDto;
import com.Travelrithm.planbuilder.dto.kakao.KakaoPlaceResopnseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


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
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(KakaoPlaceResopnseDto.class)
                .block();
    }

}
