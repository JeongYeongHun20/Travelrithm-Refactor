package com.Travelrithm.service;

import com.Travelrithm.domain.SocialType;
import com.Travelrithm.dto.NaverTokenResponseDto;
import com.Travelrithm.dto.register.NaverUserResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
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
    public String generateState(){
        SecureRandom sr=new SecureRandom();
        return new BigInteger(130, sr).toString(32);
    }

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
        URI uri = UriComponentsBuilder
                .fromUriString(NAVER_BASE_URL)
                .path("/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", client_id)
                .queryParam("client_secret", client_secret)
                .queryParam("redirect_uri", redirect_url)
                .queryParam("code", code)
                .queryParam("state", state)
                .build()
                .encode()
                .toUri();

        RestTemplate rt = new RestTemplate();
        NaverTokenResponseDto response = rt.getForObject(uri, NaverTokenResponseDto.class);

        if (response.access_token()==null){
            throw new RuntimeException("네이버 토큰을 받아오지 못함");
        }
        return getUserInfo(response.access_token());
    }

    private NaverUserResponseDto getUserInfo(String token) {
        URI uri=UriComponentsBuilder
                .fromUriString(NAVER_USER_URL)
                .path("/v1/nid/me")
                .build()
                .toUri();
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Void> request=new HttpEntity<>(headers);
        RestTemplate rt=new RestTemplate();
        ResponseEntity<NaverUserResponseDto> response=rt.exchange(
                uri,
                HttpMethod.GET,
                request,
                NaverUserResponseDto.class
        );
        return response.getBody();
    }
}
