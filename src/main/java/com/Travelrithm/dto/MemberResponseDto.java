package com.Travelrithm.dto;


import com.Travelrithm.domain.SocialType;
import com.Travelrithm.domain.Member;

import java.time.LocalDateTime;


public record MemberResponseDto(
        Long memberId,
        String name,
        String email,
        String nickname,
        SocialType socialType,
        String socialId,
        LocalDateTime createdAt
)
{
    public MemberResponseDto(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                member.getSocialType(),
                member.getSocialId(),
                member.getCreatedAt()
        );



    }
}