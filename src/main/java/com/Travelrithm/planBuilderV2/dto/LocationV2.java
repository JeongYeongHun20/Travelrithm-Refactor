package com.Travelrithm.planBuilderV2.dto;

import java.math.BigDecimal;

public record LocationV2(
        BigDecimal x, //경도(lon)
        BigDecimal y, //위도(lat)
        String name
) {
}
