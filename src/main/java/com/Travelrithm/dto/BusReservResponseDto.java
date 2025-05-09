package com.Travelrithm.dto;

import com.Travelrithm.domain.BusReservEntity.Direction;
import com.Travelrithm.domain.BusReservEntity.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BusReservResponseDto {
    private Integer reservationId;
    private Integer userId;
    private Integer planId;
    private Direction direction;
    private Integer departureTerminalId;
    private Integer arrivalTerminalId;
    private LocalDateTime departureTime;
    private String seatNumber;
    private SeatStatus seatStatus;
    private LocalDateTime createdAt;
}


