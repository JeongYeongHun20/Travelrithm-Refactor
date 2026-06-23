package com.Travelrithm.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Plan plan;

    private String placeName;
    private String placeAddress;
    private BigDecimal lat;
    private BigDecimal lng;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer day;
    private Integer sequence;
    private String category;
    private String zipcode;


}