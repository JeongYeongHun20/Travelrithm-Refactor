package com.Travelrithm.dto;


public record CommunityPostRequestDto(
        String title,
        String postContent,
        Boolean isTravelPlan,
        Integer planId
){}
