package com.Travelrithm.service;


import com.Travelrithm.domain.SocialType;
import com.Travelrithm.dto.register.MemberRegisterInfo;

public interface OAuthService {
    SocialType getProvider();
    String buildAuthorizeUrl(String state);
    MemberRegisterInfo login(String code, String state);
    String generateState();
}
