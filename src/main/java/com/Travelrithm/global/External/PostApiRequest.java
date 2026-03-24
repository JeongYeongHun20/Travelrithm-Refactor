package com.Travelrithm.global.External;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

public record PostApiRequest(
        String baseUrl,
        String path,
        MultiValueMap<String, String> queryParams,
        Object body,
        HttpHeaders headers
)implements ApiRequest{
    @Override
    public String getBaseUrl() {
        return baseUrl();
    }

    @Override
    public String getPath() {
        return path();
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers();
    }


}
