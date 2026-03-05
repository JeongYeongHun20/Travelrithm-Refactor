package com.Travelrithm.dto.register;

import com.Travelrithm.domain.SocialType;

public interface UserRegisterInfo {
    String getName();
    String getEmail();
    String getNickName();
    String getSocialId();
    SocialType getSocialType();

}
