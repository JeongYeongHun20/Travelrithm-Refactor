package com.Travelrithm.planbuilder.tmap;


import com.Travelrithm.planbuilder.dto.tmap.TmapPathRequestDto;
import com.Travelrithm.planbuilder.dto.tmap.TmapPathResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TmapPathService {

    @Value("${tmap.app_key}")
    private String appKey;
    private final WebClient.Builder webClientBuilder;
    private final String TMAP_PATH_URL="https://apis.openapi.sk.com/transit/routes";

    public TmapPathResponseDto getPath(TmapPathRequestDto tmapPathRequestDto) {
        System.out.println(tmapPathRequestDto.toString());
        log.info(appKey);
        WebClient webClient = webClientBuilder
                .baseUrl(TMAP_PATH_URL)
                .defaultHeader(HttpHeaders.ACCEPT, HttpHeaderValues.APPLICATION_JSON.toString())
                .defaultHeader("appKey", appKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
                .build();

        return webClient.post()
                .bodyValue(tmapPathRequestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(TmapPathResponseDto.class)
                .block();

    }




}
