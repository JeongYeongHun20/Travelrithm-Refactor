//package com.Travelrithm.service;
//
//
//import com.Travelrithm.domain.SocialType;
//import com.Travelrithm.dto.KakaoTokenResponseDto;
//import com.Travelrithm.dto.register.KakaoUserResponseDto;
//import com.Travelrithm.dto.register.UserRegisterInfo;
//import com.Travelrithm.global.External.ExternalApi;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.math.BigInteger;
//import java.net.URI;
//import java.security.SecureRandom;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class KakaoLoginServiceLegacy implements OAuthService{
//
//    @Value("${kakao.client_id}")
//    private String client_id;
//    @Value("${kakao.local_redirect_url}")
//    private String redirect_url;
//
//    private final ExternalApi apiExecutor;
//    @Override
//    public String generateState(){
//        SecureRandom sr=new SecureRandom();
//        return new BigInteger(130, sr).toString(32);
//    }
//    private final String KAKAO_BASE_URL = "https://kauth.kakao.com";
//    private final String KAKAO_USER_URL = "https://kapi.kakao.com";
//    @Override
//    public SocialType getProvider(){return SocialType.KAKAO;}
//    @Override
//    public String buildAuthorizeUrl(String state){
//        return UriComponentsBuilder.fromUriString(KAKAO_BASE_URL)
//                .path("/oauth/authorize")
//                .queryParam("response_type", "code")
//                .queryParam("client_id", client_id)
//                .queryParam("redirect_uri", redirect_url)
//                .queryParam("state", state)
//                .build()
//                .toUriString();
//    }
//
//    @Override
//    public UserRegisterInfo login(String code, String state) {
//        URI uri = UriComponentsBuilder
//                .fromUriString(KAKAO_BASE_URL)
//                .path("/oauth/token")
//                .build()
//                .toUri();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", client_id);
//        body.add("redirect_uri", redirect_url);
//        body.add("state", state);
//        body.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//        RestTemplate rt = new RestTemplate();
//        KakaoTokenResponseDto response = rt.postForObject(uri, request, KakaoTokenResponseDto.class);
//        if (response.access_token()==null){
//            throw new RuntimeException("카카오 토큰을 받아오지 못함");
//        }
//        return getUserInfo(response.access_token());
//    }
//
//
//    private KakaoUserResponseDto getUserInfo(String token) {
//        URI uri=UriComponentsBuilder
//                .fromUriString(KAKAO_USER_URL)
//                .path("/v2/user/me")
//                .build()
//                .toUri();
//        HttpHeaders headers=new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setBearerAuth(token);
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//        RestTemplate rt=new RestTemplate();
//        ResponseEntity<KakaoUserResponseDto> response = rt.exchange(
//                uri,
//                HttpMethod.GET,
//                requestEntity,
//                KakaoUserResponseDto.class
//        );
//        return response.getBody();
//
//    }
//
//
//}
