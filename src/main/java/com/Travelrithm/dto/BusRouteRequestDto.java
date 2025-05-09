package com.Travelrithm.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BusRouteRequestDto {
    private Integer departureTerminalId;
    private Integer arrivalTerminalId;
    private Integer companyId;
    private Integer duration;
    private BigDecimal price;
}
