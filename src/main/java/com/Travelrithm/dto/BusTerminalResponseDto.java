package com.Travelrithm.dto;

import com.Travelrithm.domain.BusTerminalEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusTerminalResponseDto {
    private Integer terminalId;
    private String name;
    private String city;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;
    private LocalDateTime createdAt;

    public BusTerminalResponseDto(BusTerminalEntity entity) {
        this.terminalId = entity.getTerminalId();
        this.name = entity.getName();
        this.city = entity.getCity();
        this.address = entity.getAddress();
        this.lat = entity.getLat();
        this.lng = entity.getLng();
        this.createdAt = entity.getCreatedAt();
    }
}
