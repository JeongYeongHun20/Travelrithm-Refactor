package com.Travelrithm.service;


import com.Travelrithm.dto.KakaoTokenResponseDto;
import com.Travelrithm.dto.KakaoUserResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final WebClient.Builder webClientBuilder;

    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.local_redirect_url}")
    private String redirect_uri;

    private final String KAKAO_BASE_URL = "https://kauth.kakao.com";
    private final String KAKAO_USER_URL = "https://kapi.kakao.com";

    public String buildAuthorizeUrl(){
        return UriComponentsBuilder.fromUriString(KAKAO_BASE_URL)
                .path("/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", redirect_uri)
                .build()
                .toUriString();
    }

    public String getAccessToken(String code) {
        WebClient webClient = webClientBuilder
                .baseUrl(KAKAO_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .build();
        KakaoTokenResponseDto kakaoTokenResponseDto=webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", client_id)
                        .with("redirect_uri", redirect_uri)
                        .with("code", code))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();
            if(kakaoTokenResponseDto.access_token()==null)
                log.info("-----------kakao token null-----------");
            return kakaoTokenResponseDto.access_token();

    }

    public KakaoUserResponseDto getUserInfo(String token) {
        WebClient webClient = webClientBuilder
                .baseUrl(KAKAO_USER_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .build();
        KakaoUserResponseDto userInfo = webClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(KakaoUserResponseDto.class)
                .block();


        return userInfo;
    }


}
