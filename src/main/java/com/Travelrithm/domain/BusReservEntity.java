package com.Travelrithm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "bus_reservation")
public class BusReservEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private PlanEntity plan;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_terminal_id")
    private BusTerminalEntity departureTerminal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_terminal_id")
    private BusTerminalEntity arrivalTerminal;

    private LocalDateTime departureTime;
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    private LocalDateTime createdAt;

    public enum Direction {
        departure, return_trip
    }

    public enum SeatStatus {
        reserved, cancelled
    }
}


