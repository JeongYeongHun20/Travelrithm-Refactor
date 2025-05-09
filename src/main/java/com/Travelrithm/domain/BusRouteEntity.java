package com.Travelrithm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_route")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BusRouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_terminal_id")
    private BusTerminalEntity departureTerminal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_terminal_id")
    private BusTerminalEntity arrivalTerminal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private BusCompanyEntity company;

    private Integer duration;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private LocalDateTime createdAt;

    public void update(BusTerminalEntity departureTerminal, BusTerminalEntity arrivalTerminal,
                       BusCompanyEntity company, Integer duration, BigDecimal price) {
        this.departureTerminal = departureTerminal;
        this.arrivalTerminal = arrivalTerminal;
        this.company = company;
        this.duration = duration;
        this.price = price;
    }
}


