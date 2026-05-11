package com.Travelrithm.dto;


import com.Travelrithm.domain.Scrap;

public record ScrapDto(
        Integer scrapId,
        Long userId,
        Integer postId

) {
    public ScrapDto(Scrap scrap){
        this(
             scrap.getScrapId(),
             scrap.getMember().getMemberId(),
             scrap.getPostEntity().getCommunityPostId()
        );
    }
}
