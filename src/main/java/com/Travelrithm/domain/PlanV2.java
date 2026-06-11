package com.Travelrithm.domain;

import jakarta.persistence.*;

@Entity
public class PlanV2 {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Member member;
}
