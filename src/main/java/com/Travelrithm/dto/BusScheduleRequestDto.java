package com.Travelrithm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BusScheduleRequestDto {
    private Integer routeId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer remainingSeats;
}
