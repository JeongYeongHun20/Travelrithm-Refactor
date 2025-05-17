package com.Travelrithm.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "region")
public class RegionEntity {
    @Id
    private Integer regionId;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String context;
    private String thumbnailImageUrl;
    private String code;

}
