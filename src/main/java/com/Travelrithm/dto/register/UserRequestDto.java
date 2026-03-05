package com.Travelrithm.dto.register;

import com.Travelrithm.domain.SocialType;


import java.time.LocalDateTime;


public record UserRequestDto(
        String name,
        String password,
        String email,
        String nickname,
        SocialType socialType,
        LocalDateTime createdAt
)implements UserRegisterInfo {
    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getEmail() {
        return this.email();
    }

    @Override
    public String getNickName() {
        return this.nickname();
    }

    @Override
    public String getSocialId() {
        return null;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.local;
    }
}
