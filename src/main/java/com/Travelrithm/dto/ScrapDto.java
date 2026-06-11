package com.Travelrithm.dto;


import com.Travelrithm.domain.Scrap;

public record ScrapDto(
        Integer scrapId,
        Long userId,
        Integer postId

) {
    public ScrapDto(Scrap scrap){
        this(
             scrap.getId(),
             scrap.getMember().getId(),
             scrap.getPost().getId()
        );
    }
}
