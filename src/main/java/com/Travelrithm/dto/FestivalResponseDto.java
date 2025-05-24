package com.Travelrithm.dto;

import com.Travelrithm.domain.FestivalEntity;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FestivalResponseDto(
        String title,
        LocalDate start,
        LocalDate end,
        String location,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String address
) {
    public static FestivalResponseDto fromEntity(FestivalEntity e) {
        return new FestivalResponseDto(
                e.getFestivalName(),
                e.getStartDate(),
                e.getEndDate(),
                e.getLocation(),
                e.getDescription(),
                e.getLatitude(),
                e.getLongitude(),
                e.getAddress()
        );
    }
}


