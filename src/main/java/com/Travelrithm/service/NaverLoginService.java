package com.Travelrithm.service;

import com.Travelrithm.domain.SocialType;
import com.Travelrithm.dto.NaverTokenResponseDto;
import com.Travelrithm.dto.register.NaverUserResponseDto;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverLoginService implements OAuthService{

    private final WebClient.Builder webClientBuilder;

    @Value("${naver.client_id}")
    private String client_id;

    @Value("${naver.client_secret}")
    private String client_secret;

    @Value("${naver.local_redirect_url}")
    private String redirect_url;

    private final String NAVER_BASE_URL = "https://nid.naver.com";
    private final String NAVER_USER_URL = "https://openapi.naver.com";


    @Override
    public SocialType getProvider(){return SocialType.NAVER;}

    @Override
    public String buildAuthorizeUrl(String state){
        return UriComponentsBuilder.fromUriString(NAVER_BASE_URL)
                .path("/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", redirect_url)
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public NaverUserResponseDto login(String code, String state) {
        WebClient webClient = webClientBuilder
                .baseUrl(NAVER_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .build();
        NaverTokenResponseDto naverTokenResponseDto = webClient.post()
                .uri("/oauth2.0/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", client_id)
                        .with("client_secret", client_secret)
                        .with("redirect_uri", redirect_url)
                        .with("code", code)
                        .with("state", state))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(NaverTokenResponseDto.class)
                .block();
        if(naverTokenResponseDto.access_token() == null)
            log.info("-----------token null-----------");
        return getUserInfo(naverTokenResponseDto.access_token());
    }

    private NaverUserResponseDto getUserInfo(String token) {
        WebClient webClient = webClientBuilder
                .baseUrl(NAVER_USER_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .build();
        NaverUserResponseDto userInfo = webClient.get()
                .uri("/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(NaverUserResponseDto.class)
                .block();

        log.info("[Naver Service] ID: " + userInfo.response().id());
        return userInfo;
    }
}
