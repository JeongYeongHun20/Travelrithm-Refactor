package com.Travelrithm.domain;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public enum SocialType {
    KAKAO("kakao", 1), NAVER("naver", 2), LOCAL("local", 3);

    private final String providerName;
    private final int code;

    SocialType(String providerName, int code) {
        this.providerName = providerName;
        this.code = code;
    }


    public static SocialType fromName(String name) {
        return Arrays.stream(SocialType.values())
                .filter(type -> type.providerName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 프로바이더: " + name));
    }

}
