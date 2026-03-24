package com.Travelrithm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@Configuration
public class WebConfig {

    @Bean
    public WebClient.Builder WebClient() {
        return WebClient
                .builder();


    }
    @Configuration
    public class RestConfig {
        @Bean
        public RestTemplate restTemplate() {
            // 실제로는 여기서 타임아웃이나 커넥션 풀 설정을 추가함
            return new RestTemplate();
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
