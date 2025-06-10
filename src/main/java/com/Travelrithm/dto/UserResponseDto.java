package com.Travelrithm.dto;


import com.Travelrithm.domain.SocialType;
import com.Travelrithm.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public record UserResponseDto(
        Integer userId,
        String name,
        String email,
        String nickname,
        SocialType socialType,
        LocalDateTime createdAt
)
{
    public UserResponseDto(UserEntity userEntity) {
        this(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getNickname(),
                userEntity.getSocialType(),
                userEntity.getCreatedAt()
        );



    }
}