package com.Travelrithm.config;

import com.Travelrithm.publicdata.v3.annotation.PublicDataRestClient;
import com.Travelrithm.publicdata.v3.properties.PublicDataProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@EnableConfigurationProperties(PublicDataProperties.class)
public class HttpClientConfig {
    @Bean
    public WebClient.Builder WebClient() {
        return WebClient
                .builder();


    }
    @Bean
    @PublicDataRestClient
    public RestClient publicDataRestClient(PublicDataProperties properties){
        DefaultUriBuilderFactory factory =
                new DefaultUriBuilderFactory(properties.baseUrl());

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .uriBuilderFactory(factory)
                .build();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
