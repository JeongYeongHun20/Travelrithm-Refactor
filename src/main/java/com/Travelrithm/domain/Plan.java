package com.Travelrithm.domain;


import com.Travelrithm.dto.PlanRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


import lombok.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_sigunguCd",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Region region;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private Integer companionCount;

    @Enumerated(EnumType.STRING)
    private TransportMode transportMode;

    @Enumerated(EnumType.STRING)
    private CompanionType companionType;

    @Enumerated(EnumType.STRING)
    private TravelPurpose travelPurpose;

    @Enumerated(EnumType.STRING)
    private TravelTaste travelTaste;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Place> placeEntities = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CommunityPost> postEntities = new ArrayList<>();

    public void update(PlanRequestDto planDto) {

        this.startDate = planDto.startDate();
        this.endDate = planDto.endDate();
        this.transportMode = planDto.transportMode();
        this.startTime = planDto.startTime();
        this.updatedAt = LocalDateTime.now();
        this.companionCount = planDto.companionCount();
        this.companionType = planDto.companionType();
        this.travelPurpose = planDto.travelPurpose();
        this.travelTaste = planDto.travelTaste();

    }

}


