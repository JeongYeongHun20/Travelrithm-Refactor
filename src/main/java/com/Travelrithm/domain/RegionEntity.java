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
@Table(name = "area_code")
public class RegionEntity {
    @Id
    private String sigunguCd;
    private String sigunguName;
    private String areaCd;
    private String areaName;

}
