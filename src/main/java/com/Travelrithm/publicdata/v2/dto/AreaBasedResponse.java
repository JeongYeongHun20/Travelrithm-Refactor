package com.Travelrithm.publicdata.v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AreaBasedResponse(
        Response response
) {
    public List<AreaBasedResponseItem> toItems() {
        if (response == null ||
                response.body() == null ||
                response.body().items() == null ||
                response.body().items().item() == null) {
            return List.of();
        }

        return response.body().items().item().stream()
                .map(Item::toItem)
                .toList();
    }

    public int totalCount() {
        if (response == null || response.body() == null) {
            return 0;
        }

        return response.body().totalCount();
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            Body body
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Body(
            Items items,
            int pageNo,
            int numOfRows,
            int totalCount
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Items(
            List<Item> item
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Item(
            Long contentid,
            String contenttypeid,
            String title,
            String addr1,
            String addr2,
            String zipcode,
            BigDecimal mapx,
            BigDecimal mapy,
            String firstimage,
            String firstimage2,
            String modifiedtime,
            String lDongRegnCd,
            String lDongSignguCd,
            String lclsSystm1,
            String lclsSystm2,
            String lclsSystm3
    ) {
        public AreaBasedResponseItem toItem() {
            return AreaBasedResponseItem
                    .builder()
                    .contentId(contentid)
                    .contentTypeId(contenttypeid)
                    .title(title)
                    .addr1(addr1)
                    .addr2(addr2)
                    .zipcode(zipcode)
                    .longitude(mapx)
                    .latitude(mapy)
                    .firstImage(firstimage)
                    .thumbnailImage(firstimage2)
                    .modifiedTime(modifiedtime)
                    .lDongRegnCd(lDongRegnCd)
                    .lDongSignguCd(lDongSignguCd)
                    .category1(lclsSystm1)
                    .category2(lclsSystm2)
                    .category3(lclsSystm3)
                    .build();
        }
    }


}