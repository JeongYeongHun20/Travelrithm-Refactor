package com.Travelrithm.global.External;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public record DeleteApiRequest() implements ApiRequest{

    @Override
    public String getBaseUrl() {
        return "";
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public HttpMethod getMethod() {
        return null;
    }

    @Override
    public HttpHeaders getHeaders() {
        return null;
    }
}
