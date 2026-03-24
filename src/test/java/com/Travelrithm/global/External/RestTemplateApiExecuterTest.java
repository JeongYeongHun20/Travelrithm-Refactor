package com.Travelrithm.global.External;

import com.Travelrithm.dto.KakaoTokenResponseDto;
import com.Travelrithm.dto.register.KakaoUserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


class RestTemplateApiExecutorConsoleTest {

    private RestTemplateApiExecutor executor;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        executor = new RestTemplateApiExecutor(restTemplate);
    }

    @Test
    void socialLogin_Test(){
        String baseUrl="https://kauth.kakao.com";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "JYH0918");
        body.add("redirect_uri", "https://localhost8081/api/auth/callback/kakao");
        body.add("state", "kakao_state");
        body.add("code", "kakao_code");

        PostApiRequest request = ApiRequest.post(baseUrl)
                .path("/oauth/token")
                .body(body)
                .headers(headers)
                .build();
        String mockResponseJson = "{\"access_token\":\"MOCK_ACCESS_TOKEN\", \"token_type\":\"bearer\", \"expires_in\":3600}";

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST)) // 메서드 일치
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(content().formData(body))
                .andRespond(withSuccess(mockResponseJson, MediaType.APPLICATION_JSON));

        //When
        KakaoTokenResponseDto response = executor.executePost(request, KakaoTokenResponseDto.class);

        // Then
        System.out.println("==================== [RESULT] ====================");
        System.out.println("Access Token : " + response.access_token());
        System.out.println("Expires In   : " + response.expires_in());
        System.out.println("Token Type   : " + response.token_type());

        System.out.println("==================================================");

        assertThat(response.access_token()).isEqualTo("MOCK_ACCESS_TOKEN");
        mockServer.verify();
    }
    @Test
    @DisplayName("콘솔에 요청과 응답을 출력하는 테스트")
    void printConsole_Test() {
        // given
        String baseUrl = "https://kauth.kakao.com";
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth("MOCK_TOKEN");
        GetApiRequest request = ApiRequest.get(baseUrl)
                .path("/v2/user/me")
                .queryParam("secure_resource","true")
                .headers(headers)
                .build();
        String mockJsonResponse = "{\"id\":12345, \"properties\":{\"nickname\":\"정영훈\"}}";

        mockServer.expect(requestTo("https://kauth.kakao.com/v2/user/me?secure_resource=true"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer MOCK_TOKEN"))
                .andRespond(withSuccess(mockJsonResponse, MediaType.APPLICATION_JSON));

        // when
        Map<String, Object> result = executor.executeGet(request, Map.class);

        // then: 결과 출력
        System.out.println("==================== [API RESPONSE RECEIVED] ====================");
        System.out.println("Response Body : " + result);
        System.out.println("User Nickname : " + ((Map)result.get("properties")).get("nickname"));
        System.out.println("=================================================================");

        mockServer.verify();
    }

}