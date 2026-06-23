package com.Travelrithm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "place_v2",
        indexes = {
                @Index(name = "idx_place_v2_content_id", columnList = "content_id"),
                @Index(name = "idx_place_v2_l_dong", columnList = "l_dong_region_code, l_dong_sigungu_code"),
                @Index(name = "idx_place_v2_category", columnList = "category1, category2, category3")
        }
)
public class PlaceV2 {

    @Id
    private Long contentId;

    // 관광공사 콘텐츠 타입 ID
    @Column(name = "content_type_id", length = 10)
    private String contentTypeId;

    // 장소명
    @Column(nullable = false, length = 200)
    private String title;

    private String addr1;
    private String addr2;

    // 우편번호
    private String zipcode;

    // X = 경도
    @Column(nullable = false, precision = 18, scale = 14)
    private BigDecimal longitude;

    // Y = 위도
    @Column(nullable = false, precision = 18, scale = 14)
    private BigDecimal latitude;

    private String firstImage;
    private String thumbnailImage;

    private String modifiedTime;

    // 법정동 시도 코드
    private String legalDongRegionCode;

    // 법정동 시군구 코드
    private String legalDongSigunguCode;

    // 관광공사 대분류/중분류/소분류
    private String category1;
    private String category2;
    private String category3;

    @Builder
    private PlaceV2(
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
        this.contentId = contentId;
        this.contentTypeId = contentTypeId;
        this.title = title;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.zipcode = zipcode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.firstImage = firstImage;
        this.thumbnailImage = thumbnailImage;
        this.modifiedTime = modifiedTime;
        this.legalDongRegionCode = lDongRegnCd;
        this.legalDongSigunguCode = lDongSignguCd;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }

    public void update(
            String title,
            String addr1,
            String addr2,
            String zipcode,
            BigDecimal longitude,
            BigDecimal latitude,
            String firstImage,
            String thumbnailImage,
            String modifiedTime,
            String legalDongRegionCode,
            String legalDongSigunguCode,
            String category1,
            String category2,
            String category3
    ) {
        this.title = title;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.zipcode = zipcode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.firstImage = firstImage;
        this.thumbnailImage = thumbnailImage;
        this.modifiedTime = modifiedTime;
        this.legalDongRegionCode = legalDongRegionCode;
        this.legalDongSigunguCode = legalDongSigunguCode;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }
}