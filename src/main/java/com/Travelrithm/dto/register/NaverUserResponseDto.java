package com.Travelrithm.dto.register;

import com.Travelrithm.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverUserResponseDto(
        String resultcode,
        String message,
        Response response
) implements  UserRegisterInfo{
    @Override
    public String getName() {
        return response().name();
    }

    @Override
    public String getEmail() {
        return response().email();
    }

    @Override
    public String getNickName() {
        return response().nickname();
    }

    @Override
    public String getSocialId() {
        return response().id();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            String id,
            String name,
            String email,
            String nickname
    ) {}
}
