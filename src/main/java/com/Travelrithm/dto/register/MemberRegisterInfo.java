package com.Travelrithm.dto.register;

import com.Travelrithm.domain.SocialType;

public interface MemberRegisterInfo {
    String getName();
    String getEmail();
    String getNickName();
    String getSocialId();
    SocialType getSocialType();

}
