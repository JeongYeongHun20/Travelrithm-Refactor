package com.Travelrithm.service;


import com.Travelrithm.dto.register.UserRegisterInfo;

public interface OAuthService {
    String getProvider();
    String buildAuthorizeUrl();
    UserRegisterInfo login(String code, String state);
}
