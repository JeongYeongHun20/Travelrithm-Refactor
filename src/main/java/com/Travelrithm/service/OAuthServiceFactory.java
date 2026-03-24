package com.Travelrithm.service;

import com.Travelrithm.domain.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OAuthServiceFactory {
    private final Map<SocialType, OAuthService> services;

    public OAuthServiceFactory(Set<OAuthService> services){
        this.services=services.stream()
                .collect(Collectors.toMap(OAuthService::getProvider, service->service));
    }
    public OAuthService getService(SocialType provider){
        return services.get(provider);
    }

}
