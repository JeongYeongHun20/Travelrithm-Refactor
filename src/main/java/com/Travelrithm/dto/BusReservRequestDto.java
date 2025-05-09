package com.Travelrithm.dto;

import com.Travelrithm.domain.BusReservEntity.Direction;
import com.Travelrithm.domain.BusReservEntity.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BusReservRequestDto {
    private Integer userId;
    private Integer planId;
    private Direction direction;
    private Integer departureTerminalId;
    private Integer arrivalTerminalId;
    private LocalDateTime departureTime;
    private String seatNumber;
    private SeatStatus seatStatus;
}
