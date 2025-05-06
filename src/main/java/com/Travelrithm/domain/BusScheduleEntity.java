package com.Travelrithm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bus_schedule")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer remainingSeats;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private BusRouteEntity route;

}

