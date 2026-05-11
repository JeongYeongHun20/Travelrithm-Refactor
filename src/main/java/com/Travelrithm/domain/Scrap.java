package com.Travelrithm.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Scrap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) //.getUser 하기전에 객체를 불러오지 않음(지연로딩)
    @JoinColumn(name = "post_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CommunityPost postEntity;



    private LocalDateTime createdAt;
}
