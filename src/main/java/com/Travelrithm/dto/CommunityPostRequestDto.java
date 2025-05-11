package com.Travelrithm.dto;

import com.Travelrithm.domain.CommunityPostEntity;
import lombok.*;


public record CommunityPostRequestDto(
        String title,
        String postContent,
        Boolean isTravelPlan,
        Integer planId
){}
