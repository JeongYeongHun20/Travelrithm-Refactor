package com.Travelrithm.dto;

import com.Travelrithm.domain.BusTerminalEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusTerminalRequestDto {
    private String name;
    private String city;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;

    public BusTerminalEntity toEntity() {
        return BusTerminalEntity.builder()
                .name(this.name)
                .city(this.city)
                .address(this.address)
                .lat(this.lat)
                .lng(this.lng)
                .build();
    }
}
