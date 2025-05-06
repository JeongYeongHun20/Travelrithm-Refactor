package com.Travelrithm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BusRouteResponseDto {
    private Integer routeId;
    private Integer departureTerminalId;
    private Integer arrivalTerminalId;
    private Integer companyId;
    private Integer duration;
    private BigDecimal price;
    private LocalDateTime createdAt;
}

