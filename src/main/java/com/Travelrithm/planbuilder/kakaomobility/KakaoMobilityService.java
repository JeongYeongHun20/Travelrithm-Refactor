package com.Travelrithm.planbuilder.kakaomobility;


import com.Travelrithm.planbuilder.dto.kakao.mobility.DestinationRequestDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.DestinationResponseDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WayPointResponseDto;
import com.Travelrithm.planbuilder.dto.kakao.mobility.WaypointRequestDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoMobilityService {

    @Value("${kakao.client_id}")
    private String client_id;
    private final WebClient.Builder webClientBuilder;

    private final String KAKAO_MOBILITY_URL="https://apis-navi.kakaomobility.com/v1";

    public DestinationResponseDto getPath(DestinationRequestDto destinationRequestDto) {
        WebClient webClient = getWebClient();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/directions")
                        .queryParam("origin", destinationRequestDto.origin())
                        .queryParam("destination", destinationRequestDto.destination())
                        .build())
                .retrieve()
                .bodyToMono(DestinationResponseDto.class)
                .block();


    }
    public WayPointResponseDto getPaths(WaypointRequestDto waypointRequestDto) {
        WebClient webClient = getWebClient();

        return webClient.post()
                .uri("/waypoints/directions")
                .bodyValue(waypointRequestDto) // JSON body로 요청
                .retrieve()
                .bodyToMono(WayPointResponseDto.class)
                .block();
    }


    private WebClient getWebClient() {
        return webClientBuilder
                .baseUrl(KAKAO_MOBILITY_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"KakaoAK "+client_id)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
                .build();
    }


}
