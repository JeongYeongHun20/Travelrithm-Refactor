package com.Travelrithm.publicdata.v2.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AreaBasedResponseItem(
        Long contentId,
        String contentTypeId,
        String title,
        String addr1,
        String addr2,
        String zipcode,
        BigDecimal longitude,
        BigDecimal latitude,
        String firstImage,
        String thumbnailImage,
        String modifiedTime,
        String lDongRegnCd,
        String lDongSignguCd,
        String category1,
        String category2,
        String category3
) {
}
