package com.Travelrithm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OAuthServiceFactory {
    private final Map<String, OAuthService> services;

    public OAuthServiceFactory(List<OAuthService> services){
        this.services=services.stream()
                .collect(Collectors.toMap(OAuthService::getProvider, service->service));
    }
    public OAuthService getService(String provider){
        return services.get(provider);
    }

}
