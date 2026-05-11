package com.Travelrithm.domain;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Region {
    @Id
    private String sigunguCd;
    private String sigunguName;
    private String areaCd;
    private String areaName;

}
