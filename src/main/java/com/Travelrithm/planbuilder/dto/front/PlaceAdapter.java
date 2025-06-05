package com.Travelrithm.planbuilder.dto.front;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PlaceAdapter {
    Integer day;
    List<PlaceInfo> places;
}
