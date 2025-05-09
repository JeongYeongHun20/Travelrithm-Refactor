package com.Travelrithm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BusScheduleResponseDto {
    private Integer scheduleId;
    private Integer routeId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer remainingSeats;
    private LocalDateTime createdAt;
}
