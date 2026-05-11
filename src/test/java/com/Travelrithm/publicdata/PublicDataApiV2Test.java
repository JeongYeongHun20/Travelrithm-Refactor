package com.Travelrithm.publicdata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PublicDataApiV2Test {

    private PublicDataApiV2 publicDataApiV2;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        publicDataApiV2 = new PublicDataApiV2();
        ReflectionTestUtils.setField(publicDataApiV2, "serviceKey", "test-service-key");

        RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(publicDataApiV2, "restTemplate");
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    void getOverView() {
        server.expect(requestTo("https://apis.data.go.kr/B551011/KorService2/detailCommon2?MobileOS=WEB&MobileApp=Travelrithm&_type=json&contentId=12345&serviceKey=test-service-key"))
                .andExpect(queryParam("contentId", "12345"))
                .andRespond(withSuccess("""
                        {
                          "response": {
                            "body": {
                              "items": {
                                "item": {
                                  "overview": "장소 상세 설명"
                                }
                              }
                            }
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        String result = publicDataApiV2.getOverView("12345");

        assertThat(result).isEqualTo("장소 상세 설명");
        server.verify();
    }

    @Test
    void getOverView_returnDefaultMessage_whenResponseBodyIsNull() {
        server.expect(requestTo("https://apis.data.go.kr/B551011/KorService2/detailCommon2?MobileOS=WEB&MobileApp=Travelrithm&_type=json&contentId=12345&serviceKey=test-service-key"))
                .andExpect(queryParam("contentId", "12345"))
                .andRespond(withSuccess("null", MediaType.APPLICATION_JSON));

        String result = publicDataApiV2.getOverView("12345");

        assertThat(result).isEqualTo("상세정보 없음");
        server.verify();
    }
}
