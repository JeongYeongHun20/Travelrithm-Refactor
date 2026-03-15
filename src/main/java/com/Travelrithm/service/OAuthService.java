package com.Travelrithm.service;


import com.Travelrithm.domain.SocialType;
import com.Travelrithm.dto.register.UserRegisterInfo;

public interface OAuthService {
    SocialType getProvider();
    String buildAuthorizeUrl(String state);
    UserRegisterInfo login(String code, String state);
    String generateState();
}
