package com.Travelrithm.publicdata.v3.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "travel-api")
public record PublicDataProperties(
        String baseUrl,
        String serviceKey,
        String mobileOs,
        String mobileApp,
        String locationBasedList2,
        String detailCommon2,
        String areaBasedList2
) {
}
