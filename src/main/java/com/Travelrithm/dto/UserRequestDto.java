package com.Travelrithm.dto;

import com.Travelrithm.domain.SocialType;


import java.time.LocalDateTime;


public record UserRequestDto(
        String name,
        String password,
        String email,
        String nickname,
        SocialType socialType,
        LocalDateTime createdAt
) {}
