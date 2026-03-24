package com.Travelrithm.global.External;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class RestTemplateApiExecutor implements ExternalApi{
    private final RestTemplate restTemplate;

    @Override
    public <T> T executeGet(GetApiRequest request, Class<T> responseType){
        URI uri=UriComponentsBuilder
                .fromUriString(request.getBaseUrl())
                .queryParams(request.getQueryParams())
                .path(request.getPath())
                .build()
                .toUri();
        HttpEntity<?> requestEntity = new HttpEntity<>(request.getHeaders());
        return restTemplate.exchange(
                uri,
                request.getMethod(),
                requestEntity,
                responseType
        ).getBody();
    }
    @Override
    public <T> T executePost(PostApiRequest request, Class<T> responseType){
        URI uri=UriComponentsBuilder
                .fromUriString(request.getBaseUrl())
                .path(request.getPath())
                .build()
                .toUri();

        HttpEntity<Object> requestEntity = new HttpEntity<>(request.body(), request.headers());

        return restTemplate.exchange(
                uri,
                request.getMethod(),
                requestEntity,
                responseType
        ).getBody();
    }

    @Override
    public <T> T executeDelete(DeleteApiRequest request, Class<T> responseType) {
        return null;
    }
}
