package com.Travelrithm.kakaomobility;


import com.Travelrithm.kakaomobility.dto.*;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoMobilityApi {

    @Value("${kakao.client_id}")
    private String client_id;
    private final WebClient.Builder webClientBuilder;

    private final String KAKAO_MOBILITY_URL="https://apis-navi.kakaomobility.com/v1";

    public DestinationResponseDto getPath(DestinationRequestDto destinationRequestDto) {
        WebClient webClient = getWebClient();
        log.info(destinationRequestDto.origin()+" "+destinationRequestDto.destination());
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/directions")
                        .queryParam("origin", destinationRequestDto.origin())
                        .queryParam("destination", destinationRequestDto.destination())
                        .queryParam("alternatives" , true)
                        .queryParam("roadevent", 2)
                        .build())
                .retrieve()
                .bodyToMono(DestinationResponseDto.class)
                .onErrorResume(e->{
                    log.error("Error: "+e);
                    return Mono.just(null);
                })
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
    public WayPointResponseDto getPaths(CompleteWaypointRequestDto completeWaypointRequestDto) {
        WebClient webClient = getWebClient();

        return webClient.post()
                .uri("/waypoints/directions")
                .bodyValue(completeWaypointRequestDto) // JSON body로 요청
                .retrieve()
                .bodyToMono(WayPointResponseDto.class)
                .block();
    }

    public WayPointResponseDto getPathsV2(WaypointRequestV2 waypointRequestV2) {
        WebClient webClient = getWebClient();

        return webClient.post()
                .uri("/waypoints/directions")
                .bodyValue(waypointRequestV2)
                .retrieve()
                .bodyToMono(WayPointResponseDto.class)
                .block();
    }


    private WebClient getWebClient() {
        UriComponentsBuilder.fromUriString(KAKAO_MOBILITY_URL);
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION,"kakaoAK"+client_id);
        return webClientBuilder
                .baseUrl(KAKAO_MOBILITY_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"KakaoAK "+client_id)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
                .build();
    }


}
